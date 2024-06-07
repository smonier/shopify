package org.jahia.se.modules.shopify.services;

import java.io.IOException;
import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;

public interface IShopifyService {

    String getProducts(String shop) throws IOException;

    String createProduct(String shop, String productJson) throws IOException;

    String updateProduct(String shop, String productId, String productJson) throws IOException;

    String deleteProduct(String shop, String productId) throws IOException;

    void updated(Dictionary<String, ?> dictionary) throws ConfigurationException;
}
