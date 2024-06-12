package org.jahia.se.modules.shopify.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductDeserializer {

    public static List<Product> fetchProductsFromJson(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode productsNode = rootNode.path("products");

            if (productsNode.isArray()) {
                return objectMapper.readValue(productsNode.toString(), new TypeReference<List<Product>>() {});
            } else {
                List<Product> productList = new ArrayList<>();
                Product product = objectMapper.readValue(productsNode.toString(), Product.class);
                productList.add(product);
                return productList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}