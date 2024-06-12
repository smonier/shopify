package org.jahia.se.modules.shopify.services;

import java.io.IOException;
import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;

public interface IShopifyService {

    String getProducts(String shop) throws IOException;

    String getProductsFromHandle(String shop, String handle) throws IOException;

    String createProduct(String shop, String productJson) throws IOException;

    String updateProduct(String shop, long productId, String productJson) throws IOException;

    String deleteProduct(String shop, long productId) throws IOException;

    void updated(Dictionary<String, ?> dictionary) throws ConfigurationException;

    int getVariantsCount(String shop, long productId) throws IOException;

    String createVariant(String shop, long productId, String productJson) throws IOException;

}
