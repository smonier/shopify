package org.jahia.se.modules.shopify.services.impl;

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
    public String deleteProduct(String shop, long productId) throws IOException {
        String urlStr = "https://" + shop + ".myshopify.com/admin/products/" + productId + ".json";
        return sendRequest(shop, urlStr, "DELETE", null);
    }

    @Override
    public void updateMetafield(String shop, String productId, String metafieldId, String value, String type) throws Exception {
        String accessToken = getValue(shop);

        // Create JSON payload
        String jsonPayload = String.format(
                "{\"metafield\":{\"id\":%s,\"value\":\"%s\",\"type\":\"%s\"}}",
                metafieldId, value, type
        );

        // Construct the URL with the provided shop name and product ID
        String shopifyApiUrl = String.format(
                "https://%s.myshopify.com/admin/api/2024-04/products/%s/metafields/%s.json",
                shop, productId, metafieldId
        );
        logger.error("Metafields: "+jsonPayload+shopifyApiUrl);

        // Set up the connection
        URL url = new URL(shopifyApiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("X-Shopify-Access-Token", accessToken);
        conn.setRequestProperty("Content-Type", "application/json");

        // Send the request
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }

        // Get the response
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        System.out.println("Metafield updated successfully.");
        conn.disconnect();
    }

    @Override
    public void createMetafield(String shopName, String productId, String namespace, String key, String value, String type) throws Exception {
        String accessToken = getValue(shopName);

        // Create JSON payload
        String jsonPayload = String.format(
                "{\"metafield\":{\"namespace\":\"%s\",\"key\":\"%s\",\"value\":\"%s\",\"type\":\"%s\"}}",
                namespace, key, value, type
        );

        // Construct the URL with the provided shop name and product ID
        String shopifyApiUrl = String.format(
                "https://%s.myshopify.com/admin/api/2024-01/products/%s/metafields.json",
                shopName, productId
        );

        // Set up the connection
        URL url = new URL(shopifyApiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("X-Shopify-Access-Token", accessToken);
        conn.setRequestProperty("Content-Type", "application/json");

        // Send the request
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }

        // Get the response
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_CREATED) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        System.out.println("Metafield created successfully.");
        conn.disconnect();
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
