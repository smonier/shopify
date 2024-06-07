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
        // LOGGER.info("shopName: " + shopName);
        List<String> shopNameList = new ArrayList<>();

        List<String> shopList = convertStringToList(shopName);

        LOGGER.info("shopList: " + shopList);

        for (String shop : shopList) {
            JCRNodeWrapper fileNode = getNodeByUUID(shop, session);
            shopNameList.add(fileNode.getDisplayableName());
        }
        LOGGER.info("shopNameList: " + shopNameList);

        /*
         * String fileUuid = node.getPropertyAsString("csvFile");
         * JCRNodeWrapper fileNode = getNodeByUUID(fileUuid, session);
         * String filePath = fileNode.getUrl();
         * LOGGER.info("filePath: " + filePath);
         * 
         * String serverUrl = getBaseUrl(request);
         * LOGGER.info("serverUrl: " + serverUrl);
         */
        File csvFile = JCRContentUtils.downloadFileContent((JCRFileNode) node.getProperty("csvFile").getNode());
        // String fileUrl = serverUrl + filePath;

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile))
                .withSkipLines(1) // Skip header row
                .withCSVParser(new com.opencsv.CSVParserBuilder().withSeparator(';').build()) // Specify semicolon
                .build()) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                if (values.length >= 7) {
                    String id = values[0];
                    String title = values[1];
                    String body_html = values[2];
                    String vendor = values[3];
                    String product_type = values[4];
                    String price = values[5];
                    String inventory_quantity = values[6];
                    String action = values[7];


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
                        }
                    } });
                    productData.put("product", productDetails);

                    // Serialize productData to JSON
                    String productDataJson = objectMapper.writeValueAsString(productData);

                    // Update the product on Shopify
                    for (String shop : shopNameList) {
                        LOGGER.info("Shop Update: " + shop);
                        switch (action.toLowerCase()) {
                            case "create":
                                shopifyService.createProduct(shop, productDataJson);
                                break;
                            case "read":
                                shopifyService.getProducts(shop);
                                break;
                            case "update":
                                shopifyService.updateProduct(shop, id, productDataJson);
                                break;
                            case "delete":
                                shopifyService.deleteProduct(shop, id);
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid action: " + action);
                        }
                    }
                    resultCode = HttpServletResponse.SC_OK;
                } else {
                    LOGGER.error("Invalid row: " + String.join(",", values));
                }
            }
            return resultCode;
        } catch (IOException | CsvValidationException e) {
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
}
