package org.jahia.se.modules.shopify.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jahia.se.modules.shopify.services.IShopifyService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Scanner;

@Component(service = {ShopifyService.class, ManagedService.class}, property = "service.pid=org.jahia.se.modules.shopify", immediate = true)
public class ShopifyService implements IShopifyService, ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(ShopifyService.class);
    private String[][] shopAccessToken = null;

    @Override
    public String getProducts(String shop) throws IOException {
        String urlStr = "https://" + shop + ".myshopify.com/admin/products.json";
        return sendRequest(shop, urlStr, "GET", null);
    }

    @Override
    public String getProductsFromHandle(String shop, String handle) throws IOException {
        String urlStr = "https://" + shop + ".myshopify.com/admin/products.json?handle="+handle;
        return sendRequest(shop, urlStr, "GET", null);
    }

    @Override
    public String createProduct(String shop, String productJson) throws IOException {
        String urlStr = "https://" + shop + ".myshopify.com/admin/products.json";
        return sendRequest(shop, urlStr, "POST", productJson);
    }

    @Override
    public String updateProduct(String shop, long productId, String productJson) throws IOException {
        String urlStr = "https://" + shop + ".myshopify.com/admin/products/" + productId + ".json";
        return sendRequest(shop, urlStr, "PUT", productJson);
    }
    @Override
    public String createVariant(String shop, long productId, String productJson) throws IOException {
        String urlStr = "https://" + shop + ".myshopify.com/admin/products/" + productId + ".json";
        return sendRequest(shop, urlStr, "PUT", productJson);
    }
    @Override
    public String deleteProduct(String shop, long productId) throws IOException {
        String urlStr = "https://" + shop + ".myshopify.com/admin/products/" + productId + ".json";
        return sendRequest(shop, urlStr, "DELETE", null);
    }

    @Override
    public int getVariantsCount(String shop, long productId) throws IOException {

        String urlStr = "https://" + shop + ".myshopify.com/admin/products/" + productId + "/variants/count.json";
        String jsonResponse = sendRequest(shop, urlStr, "GET", null);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            return rootNode.path("count").asInt();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String sendRequest(String shop, String urlStr, String method, String payload) throws IOException {
        logger.info("Sending {} request to URL: {} - payload {}", method, urlStr, payload);
        String accessToken = getValue(shop);
        logger.info("AccessToken {}: ", accessToken);

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("X-Shopify-Access-Token", accessToken);
        conn.setRequestProperty("Content-Type", "application/json");

        if (payload != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }
        }

        int responseCode = conn.getResponseCode();
        logger.info("Response Code: {}", responseCode);

        if (responseCode < HttpURLConnection.HTTP_OK || responseCode >= HttpURLConnection.HTTP_MULT_CHOICE) {
            try (Scanner scanner = new Scanner(conn.getErrorStream())) {
                String errorResponse = scanner.useDelimiter("\\A").next();
                logger.error("Error Response: {}", errorResponse);
                throw new IOException("Failed to send request: " + errorResponse);
            }
        }

        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            String response = scanner.useDelimiter("\\A").next();
            logger.info("Response: {}", response);
            return response;
        }
    }

    @Override
    public void updated(Dictionary<String, ?> dictionary) throws ConfigurationException {
        if (dictionary == null || dictionary.isEmpty()) {
            logger.error("Configuration dictionary is null or empty.");
            return;
        }

        // Determine the size of the dictionary
        int size = dictionary.size();
        shopAccessToken = new String[size][2];
        
        Enumeration<String> keys = dictionary.keys();
        int index = 0;

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = (String) dictionary.get(key);
            shopAccessToken[index][0] = key;
            shopAccessToken[index][1] = value;
            index++;
        }

        // Log the configuration array for debugging
        for (String[] config : shopAccessToken) {
            logger.debug("Property: {} = {}", config[0], config[1]);
        }
    }

    public String getValue(String key) {
        if (shopAccessToken == null) {
            logger.error("Configuration array is not initialized.");
            return null;
        }

        for (String[] config : shopAccessToken) {
            if (config[0].equals(key)) {
                return config[1];
            }
        }

        logger.error("Key {} not found in configuration.", key);
        return null;
    }

}
