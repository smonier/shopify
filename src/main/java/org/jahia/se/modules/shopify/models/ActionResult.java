package org.jahia.se.modules.shopify.models;

import java.util.List;

public class ActionResult {
    private int resultCode;
    private List<Shop> shop;

    public ActionResult(int resultCode, List<Shop> shop) {
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
