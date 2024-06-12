package org.jahia.se.modules.shopify.model;

import java.util.List;

public class UpdateResult {
    private int resultCode;
    private List<Shop> shop;

    public UpdateResult(int resultCode, List<Shop> shop) {
        this.resultCode = resultCode;
        this.shop = shop;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<Shop> getShop() {
        return shop;
    }

    public void setShop(List<Shop> shop) {
        this.shop = shop;
    }
}
