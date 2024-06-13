package org.jahia.se.modules.shopify.actions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.jcr.ItemNotFoundException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.se.modules.shopify.models.*;
import org.jahia.se.modules.shopify.services.impl.ShopifyService;
import org.jahia.services.content.JCRContentUtils;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.decorator.JCRFileNode;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Component(service = Action.class, immediate = true)
public class RequestShopifyProductsUpdate extends Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestShopifyProductsUpdate.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static int productCount = 0;

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

        ActionResponse result = updateProductsFromCSV(resource.getNode(), session, request);
        resp.put("resultCode", result.getResultCode());
        resp.put("shop", result.getShop());
        LOGGER.info(resp.toString());
        return new ActionResult(result.getResultCode(), null, resp);
    }

    private ActionResponse updateProductsFromCSV(JCRNodeWrapper node, JCRSessionWrapper session, HttpServletRequest request)
            throws IOException, ItemNotFoundException, ValueFormatException, PathNotFoundException,
            RepositoryException {
        int resultCode = HttpServletResponse.SC_BAD_REQUEST;
        final String shopName = node.getPropertyAsString("shopName");
        List<String> shopNameList = new ArrayList<>();
        Map<String, Integer> shopProductCounts = new HashMap<>();

        List<String> shopList = convertStringToList(shopName);
        LOGGER.info("shopList: " + shopList);

        for (String shop : shopList) {
            JCRNodeWrapper fileNode = getNodeByUUID(shop, session);
            shopNameList.add(fileNode.getDisplayableName());
            shopProductCounts.put(fileNode.getDisplayableName(), 0);
        }

        File csvFile = JCRContentUtils.downloadFileContent((JCRFileNode) node.getProperty("csvFile").getNode());

        try {
            JsonNode rootNode = convertCsvToJson(csvFile);
            ObjectMapper objectMapper = new ObjectMapper();
            List<ProductFromCsv> productsFromCsv = ProductFromCsvDeserializer.fetchProductsFromJson(rootNode.toString());
            LOGGER.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));

            if (productsFromCsv != null) {
                for (ProductFromCsv product : productsFromCsv) {
                    if (product.getTitle() != null && !product.getTitle().isEmpty()) {
                        for (String shop : shopNameList) {
                            LOGGER.info(String.format("************* CSV Handle: %s, CSV Title: %s ****************",
                                    product.getHandle(), product.getTitle()));
                            String shopifyObject = shopifyService.getProductsFromHandle(shop, product.getHandle());
                            if (isProductsArrayEmpty(shopifyObject)) {
                                String productDataJson = generateNewProductDataJson(product);
                                shopifyService.createProduct(shop, productDataJson);
                                resultCode = HttpServletResponse.SC_OK;
                                shopProductCounts.put(shop, shopProductCounts.get(shop) + 1);
                            } else {
                                List<Product> shopifyProducts = ProductDeserializer.fetchProductsFromJson(shopifyObject);
                                for (Product shopifyProduct : shopifyProducts) {
                                    String productDataJson = generateProductDataJson(product, shopifyProduct);
                                    shopifyService.updateProduct(shop, shopifyProduct.getId(), productDataJson);
                                    resultCode = HttpServletResponse.SC_OK;
                                    shopProductCounts.put(shop, shopProductCounts.get(shop) + 1);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create JSON result structure
        List<Shop> shopListWithCounts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : shopProductCounts.entrySet()) {
            shopListWithCounts.add(new Shop(entry.getKey(), entry.getValue()));
        }
        ActionResponse updateResult = new ActionResponse(resultCode, shopListWithCounts);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(Collections.singletonMap("updateResult", Collections.singletonList(updateResult)));
        LOGGER.info("UpdateResult JSON: " + jsonString);

        return updateResult;
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

    public static JsonNode convertCsvToJson(File csvFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.createArrayNode();
        ObjectNode rootNode = objectMapper.createObjectNode();

        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader().withDelimiter(';');

        try (FileReader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, csvFormat)) {

            for (CSVRecord csvRecord : csvParser) {
                ObjectNode jsonObject = objectMapper.createObjectNode();
                for (String header : csvParser.getHeaderMap().keySet()) {
                    jsonObject.put(header, csvRecord.get(header));
                }
                jsonArray.add(jsonObject);
            }
        }

        rootNode.set("products", jsonArray);
        return rootNode;
    }

    public static String generateProductDataJson(ProductFromCsv productFromCsv, Product product) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create product data map
        Map<String, Object> productData = new HashMap<>();
        Map<String, Object> productDetails = new HashMap<>();
        productDetails.put("id", product.getId());
        productDetails.put("title", productFromCsv.getTitle());
        productDetails.put("body_html", productFromCsv.getBodyHtml());
        productDetails.put("vendor", productFromCsv.getVendor());
        productDetails.put("product_type", productFromCsv.getType());
        productDetails.put("tags", productFromCsv.getTags());

        Map<String, Object> variantDetails = new HashMap<>();
        for (Product.Variant variant : product.getVariants()) {
            if (variant.getTitle() != "DefaultTitle") {
                variantDetails.put("id", variant.getId());
                variantDetails.put("product_id", variant.getProductId());
                variantDetails.put("option1", productFromCsv.getOption1Value());
                variantDetails.put("option2", productFromCsv.getOption2Value());
                variantDetails.put("option2", productFromCsv.getOption3Value());
                variantDetails.put("price", productFromCsv.getVariantPrice());
                variantDetails.put("inventory_quantity", productFromCsv.getVariantInventoryQty());
                variantDetails.put("sku", productFromCsv.getVariantSku());

            } else {
                variantDetails.put("price", productFromCsv.getVariantPrice());
                variantDetails.put("inventory_quantity", productFromCsv.getVariantInventoryQty());
                variantDetails.put("sku", productFromCsv.getVariantSku());
            }
        }
        Map<String, Object> imageDetails = new HashMap<>();
        imageDetails.put("src", productFromCsv.getImageSrc());

        productDetails.put("variants", new Object[]{variantDetails});
        productDetails.put("images", new Object[]{imageDetails});
        productData.put("product", productDetails);

        // Serialize productData to JSON
        return objectMapper.writeValueAsString(productData);
    }

    public static String generateVariantDataJson(ProductFromCsv productFromCsv) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create product data map
        Map<String, Object> variantData = new HashMap<>();
        Map<String, Object> variantDetails = new HashMap<>();

        variantDetails.put("option1", productFromCsv.getOption1Value());
        variantDetails.put("option2", productFromCsv.getOption2Value());
        variantDetails.put("option2", productFromCsv.getOption3Value());
        variantDetails.put("price", productFromCsv.getVariantPrice());
        variantDetails.put("inventory_quantity", productFromCsv.getVariantInventoryQty());
        variantDetails.put("sku", productFromCsv.getVariantSku());

        variantData.put("variant", variantDetails);

        // Serialize productData to JSON
        return objectMapper.writeValueAsString(variantData);
    }

    public static String generateNewProductDataJson(ProductFromCsv productFromCsv) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create product data map
        Map<String, Object> productData = new HashMap<>();
        Map<String, Object> productDetails = new HashMap<>();
        productDetails.put("handle", productFromCsv.getHandle());
        productDetails.put("title", productFromCsv.getTitle());
        productDetails.put("body_html", productFromCsv.getBodyHtml());
        productDetails.put("vendor", productFromCsv.getVendor());
        productDetails.put("product_type", productFromCsv.getType());
        productDetails.put("tags", productFromCsv.getTags());

        Map<String, Object> variantDetails = new HashMap<>();

        variantDetails.put("option1", productFromCsv.getOption1Value());
        variantDetails.put("option2", productFromCsv.getOption2Value());
        variantDetails.put("option2", productFromCsv.getOption3Value());
        variantDetails.put("price", productFromCsv.getVariantPrice());
        variantDetails.put("inventory_quantity", productFromCsv.getVariantInventoryQty());
        variantDetails.put("sku", productFromCsv.getVariantSku());

        Map<String, Object> imageDetails = new HashMap<>();
        imageDetails.put("src", productFromCsv.getImageSrc());

        productDetails.put("variants", new Object[]{variantDetails});
        productDetails.put("images", new Object[]{imageDetails});

        productData.put("product", productDetails);

        // Serialize productData to JSON
        return objectMapper.writeValueAsString(productData);
    }

    public static boolean isProductsArrayEmpty(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        JsonNode productsNode = rootNode.path("products");
        return productsNode.isArray() && productsNode.size() == 0;
    }
}
