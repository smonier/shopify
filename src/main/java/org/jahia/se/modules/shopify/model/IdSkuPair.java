package org.jahia.se.modules.shopify.model;

public class IdSkuPair {
        private long id;
        private String sku;

        public IdSkuPair(long id, String sku) {
            this.id = id;
            this.sku = sku;
        }

        public long getId() {
            return id;
        }

        public String getSku() {
            return sku;
        }
    }