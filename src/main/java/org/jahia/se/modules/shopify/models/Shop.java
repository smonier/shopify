package org.jahia.se.modules.shopify.models;

public class Shop {
    private String name;
    private int productCount;

    public Shop(String name, int productCount) {
        this.name = name;
        this.productCount = productCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
