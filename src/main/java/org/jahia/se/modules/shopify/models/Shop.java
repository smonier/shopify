package org.jahia.se.modules.shopify.models;

public class Shop {
    private String name;
    private int productCreated;
    private int productUpdated;


    public Shop(String name, int productCreated, int productUpdated) {
        this.name = name;
        this.productCreated = productCreated;
        this.productUpdated = productUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductCreated() {
        return productCreated;
    }

    public void setProductCreated(int productCreated) {
        this.productCreated = productCreated;
    }

    public int getProductUpdated() {
        return productUpdated;
    }

    public void setProductUpdated(int productUpdated) {
        this.productUpdated = productUpdated;
    }
}
