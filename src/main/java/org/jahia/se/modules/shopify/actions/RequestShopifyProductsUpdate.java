package org.jahia.se.modules.shopify.actions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jcr.ItemNotFoundException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.se.modules.shopify.model.IdSkuPair;
import org.jahia.se.modules.shopify.model.Product;
import org.jahia.se.modules.shopify.model.ProductDeserializer;
import org.jahia.se.modules.shopify.services.impl.ShopifyService;
import org.jahia.services.content.JCRContentUtils;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.decorator.JCRFileNode;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

@Component(service = Action.class, immediate = true)
public class RequestShopifyProductsUpdate extends Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestShopifyProductsUpdate.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Activate
    public void activate() {
        setName("requestShopifyProductsUpdate");
        setRequireAuthenticatedUser(true);
        setRequiredPermission("jcr:write_default");
        setRequiredWorkspace("default");
        setRequiredMethods("GET,POST");
    }

    private ShopifyService shopifyService;

    @Reference(service = ShopifyService.class)
    public void setShopifyService(ShopifyService shopifyService) {
        this.shopifyService = shopifyService;
    }

    public ShopifyService getShopifyService() {
        return shopifyService;
    }

    @Override
    public ActionResult doExecute(final HttpServletRequest request, final RenderContext renderContext,
                                  final Resource resource, final JCRSessionWrapper session, Map<String, List<String>> parameters,
                                  final URLResolver urlResolver) throws Exception {
        final JSONObject resp = new JSONObject();
        int resultCode = HttpServletResponse.SC_BAD_REQUEST;

        resultCode = updateProductsFromCSV(resource.getNode(), session, request);

        return new ActionResult(resultCode, null, resp);
    }

    private int updateProductsFromCSV(JCRNodeWrapper node, JCRSessionWrapper session, HttpServletRequest request)
            throws IOException, ItemNotFoundException, ValueFormatException, PathNotFoundException,
            RepositoryException {
        int resultCode = HttpServletResponse.SC_BAD_REQUEST;
        final String shopName = node.getPropertyAsString("shopName");
        List<String> shopNameList = new ArrayList<>();

        List<String> shopList = convertStringToList(shopName);
        List<Product> productList = new ArrayList<>();
        LOGGER.info("shopList: " + shopList);

        for (String shop : shopList) {
            JCRNodeWrapper fileNode = getNodeByUUID(shop, session);
            shopNameList.add(fileNode.getDisplayableName());
        }
        for (String shop : shopNameList) {
            LOGGER.info("ACCESSING SHOP: " + shop);
            String jsonUrl = shopifyService.getProducts(shop); // Method to get the JSON URL for the shop
            List<Product> products = ProductDeserializer.fetchProductsFromJson(jsonUrl);
            if (products != null) {
                productList.addAll(products);
            }
        }
        if (productList != null) {
            for (Product product : productList) {
                LOGGER.info(String.format("PRODUCT ID: %d, Title: %s",
                        product.getId(), product.getTitle()));
                for (Product.Variant variant : product.getVariants()) {
                    LOGGER.info(String.format("Variant ID: %d, Title: %s, Product ID: %d, SKU: %s",
                            variant.getId(), variant.getTitle(), variant.getProductId(), variant.getSku()));

                }
            }
        }
        File csvFile = JCRContentUtils.downloadFileContent((JCRFileNode) node.getProperty("csvFile").getNode());

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile))
                .withSkipLines(1) // Skip header row
                .withCSVParser(new com.opencsv.CSVParserBuilder().withSeparator(';').build()) // Specify semicolon
                .build()) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                LOGGER.info("values.length: " + values.length);

                if (values.length >= 8) {
                    String id = values[0];
                    String title = values[1];
                    String body_html = values[2];
                    String vendor = values[3];
                    String product_type = values[4];
                    String price = values[5];
                    String inventory_quantity = values[6];
                    String action = values[7];
                    String amazingId = values[8];

                    // Create product data map
                    Map<String, Object> productData = new HashMap<>();
                    Map<String, Object> productDetails = new HashMap<>();
                    productDetails.put("id", id);
                    productDetails.put("title", title);
                    productDetails.put("body_html", body_html);
                    productDetails.put("vendor", vendor);
                    productDetails.put("product_type", product_type);
                    productDetails.put("variants", new Object[] { new HashMap<String, Object>() {
                        {
                            put("price", price);
                            put("inventory_quantity", inventory_quantity);
                            put("sku", amazingId);
                        }
                    } });
                    productData.put("product", productDetails);

                    // Serialize productData to JSON
                    String productDataJson = objectMapper.writeValueAsString(productData);

                    // Update the product on Shopify
                    for (String shop : shopNameList) {
                        List<IdSkuPair> idSkuList = extractIdSkuPairs(shopifyService.getProducts(shop));
                        Long shopifyId = getIdBySku(idSkuList, amazingId);

                        if (shopifyId != null) {
                            LOGGER.info("Shop Update: " + shop);
                            switch (action.toLowerCase()) {
                                case "create":
                                    shopifyService.createProduct(shop, productDataJson);
                                    break;
                                case "read":
                                    shopifyService.getProducts(shop);
                                    break;
                                case "update":
                                    shopifyService.updateProduct(shop, shopifyId, productDataJson);
                                    break;
                                case "delete":
                                    shopifyService.deleteProduct(shop, shopifyId);
                                    break;
                                default:
                                    throw new IllegalArgumentException("Invalid action: " + action);
                            }
                            resultCode = HttpServletResponse.SC_OK;
                        } else {
                            LOGGER.error("SKU " + amazingId + " not found in shop " + shop);
                        }
                    }
                } else {
                    LOGGER.error("Invalid row: " + String.join(",", values));
                }
            }
            return resultCode;
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            return resultCode;

        } catch (Exception e) {
            e.printStackTrace();
            return resultCode;
        }
    }

    public static String getBaseUrl(HttpServletRequest req) {
        String baseUrl = null;
        if (req != null) {
            String url = req.getRequestURL().toString();
            String uri = req.getRequestURI();
            baseUrl = url.substring(0, url.length() - uri.length());
        }
        return baseUrl;
    }

    private JCRNodeWrapper getNodeByUUID(String identifier, JCRSessionWrapper session) {
        try {
            return session.getNodeByUUID(identifier);
        } catch (RepositoryException e) {
            LOGGER.error("Error retrieving node with UUID " + identifier, e);
        }
        return null;
    }

    public static List<String> convertStringToList(String str) {
        // Split the string by spaces
        String[] elements = str.split(" ");
        // Convert the array to a list and wrap in an ArrayList
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static List<IdSkuPair> extractIdSkuPairs(String response) {
        List<IdSkuPair> idSkuList = new ArrayList<>();
        try {
            // Parse JSON content
            JSONObject jsonObject = new JSONObject(response);
            JSONArray products = jsonObject.getJSONArray("products");

            // Extract id and sku pairs
            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                long productId = product.getLong("id");
                JSONArray variants = product.getJSONArray("variants");
                for (int j = 0; j < variants.length(); j++) {
                    JSONObject variant = variants.getJSONObject(j);
                    String sku = variant.getString("sku");
                    idSkuList.add(new IdSkuPair(productId, sku));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idSkuList;
    }

    public static Long getIdBySku(List<IdSkuPair> idSkuList, String sku) {
        for (IdSkuPair pair : idSkuList) {
            if (pair.getSku().equals(sku)) {
                return pair.getId();
            }
        }
        return null; // Return null if SKU not found
    }
}
