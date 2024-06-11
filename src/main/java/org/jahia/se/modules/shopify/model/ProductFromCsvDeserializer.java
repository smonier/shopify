package org.jahia.se.modules.shopify.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class ProductFromCsvDeserializer {

    public static List<ProductFromCsv> fetchProductsFromJson(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode productsNode = rootNode.path("products");

            return objectMapper.readValue(productsNode.toString(), new TypeReference<List<ProductFromCsv>>() {});


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}