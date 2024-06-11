package org.jahia.se.modules.shopify.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class Product {
    private long id;
    private String title;
    @JsonProperty("body_html")
    private String bodyHtml;
    private String vendor;
    @JsonProperty("product_type")
    private String productType;
    @JsonProperty("created_at")
    private Date createdAt;
    private String handle;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("published_at")
    private Date publishedAt;
    @JsonProperty("template_suffix")
    private String templateSuffix;
    @JsonProperty("published_scope")
    private String publishedScope;
    private String tags;
    private String status;
    @JsonProperty("admin_graphql_api_id")
    private String adminGraphqlApiId;
    private List<Variant> variants;
    private List<Option> options;
    private List<Image> images;
    private Image image;

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTemplateSuffix() {
        return templateSuffix;
    }

    public void setTemplateSuffix(String templateSuffix) {
        this.templateSuffix = templateSuffix;
    }

    public String getPublishedScope() {
        return publishedScope;
    }

    public void setPublishedScope(String publishedScope) {
        this.publishedScope = publishedScope;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminGraphqlApiId() {
        return adminGraphqlApiId;
    }

    public void setAdminGraphqlApiId(String adminGraphqlApiId) {
        this.adminGraphqlApiId = adminGraphqlApiId;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    // Nested classes
    public static class Variant {
        private long id;
        @JsonProperty("product_id")
        private long productId;
        private String title;
        private String price;
        private String sku;
        private int position;
        @JsonProperty("inventory_policy")
        private String inventoryPolicy;
        @JsonProperty("compare_at_price")
        private String compareAtPrice;
        @JsonProperty("fulfillment_service")
        private String fulfillmentService;
        @JsonProperty("inventory_management")
        private String inventoryManagement;
        private String option1;
        private String option2;
        private String option3;
        @JsonProperty("created_at")
        private Date createdAt;
        @JsonProperty("updated_at")
        private Date updatedAt;
        private boolean taxable;
        private String barcode;
        private int grams;
        private double weight;
        @JsonProperty("weight_unit")
        private String weightUnit;
        @JsonProperty("inventory_item_id")
        private long inventoryItemId;
        @JsonProperty("inventory_quantity")
        private int inventoryQuantity;
        @JsonProperty("old_inventory_quantity")
        private int oldInventoryQuantity;
        @JsonProperty("requires_shipping")
        private boolean requiresShipping;
        @JsonProperty("admin_graphql_api_id")
        private String adminGraphqlApiId;
        @JsonProperty("image_id")
        private String imageId;

        // Getters and setters
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getInventoryPolicy() {
            return inventoryPolicy;
        }

        public void setInventoryPolicy(String inventoryPolicy) {
            this.inventoryPolicy = inventoryPolicy;
        }

        public String getCompareAtPrice() {
            return compareAtPrice;
        }

        public void setCompareAtPrice(String compareAtPrice) {
            this.compareAtPrice = compareAtPrice;
        }

        public String getFulfillmentService() {
            return fulfillmentService;
        }

        public void setFulfillmentService(String fulfillmentService) {
            this.fulfillmentService = fulfillmentService;
        }

        public String getInventoryManagement() {
            return inventoryManagement;
        }

        public void setInventoryManagement(String inventoryManagement) {
            this.inventoryManagement = inventoryManagement;
        }

        public String getOption1() {
            return option1;
        }

        public void setOption1(String option1) {
            this.option1 = option1;
        }

        public String getOption2() {
            return option2;
        }

        public void setOption2(String option2) {
            this.option2 = option2;
        }

        public String getOption3() {
            return option3;
        }

        public void setOption3(String option3) {
            this.option3 = option3;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public boolean isTaxable() {
            return taxable;
        }

        public void setTaxable(boolean taxable) {
            this.taxable = taxable;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public int getGrams() {
            return grams;
        }

        public void setGrams(int grams) {
            this.grams = grams;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public String getWeightUnit() {
            return weightUnit;
        }

        public void setWeightUnit(String weightUnit) {
            this.weightUnit = weightUnit;
        }

        public long getInventoryItemId() {
            return inventoryItemId;
        }

        public void setInventoryItemId(long inventoryItemId) {
            this.inventoryItemId = inventoryItemId;
        }

        public int getInventoryQuantity() {
            return inventoryQuantity;
        }

        public void setInventoryQuantity(int inventoryQuantity) {
            this.inventoryQuantity = inventoryQuantity;
        }

        public int getOldInventoryQuantity() {
            return oldInventoryQuantity;
        }

        public void setOldInventoryQuantity(int oldInventoryQuantity) {
            this.oldInventoryQuantity = oldInventoryQuantity;
        }

        public boolean isRequiresShipping() {
            return requiresShipping;
        }

        public void setRequiresShipping(boolean requiresShipping) {
            this.requiresShipping = requiresShipping;
        }

        public String getAdminGraphqlApiId() {
            return adminGraphqlApiId;
        }

        public void setAdminGraphqlApiId(String adminGraphqlApiId) {
            this.adminGraphqlApiId = adminGraphqlApiId;
        }

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }
    }


    public static class Option {
        private long id;
        private long productId;
        private String name;
        private int position;
        private List<String> values;

        // Getters and setters
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }
    }

    public static class Image {
        private long id;
        private String alt;
        private int position;
        private long productId;
        private Date createdAt;
        private Date updatedAt;
        private String adminGraphqlApiId;
        private int width;
        private int height;
        private String src;
        private List<Long> variantIds;

        // Getters and setters
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getAdminGraphqlApiId() {
            return adminGraphqlApiId;
        }

        public void setAdminGraphqlApiId(String adminGraphqlApiId) {
            this.adminGraphqlApiId = adminGraphqlApiId;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public List<Long> getVariantIds() {
            return variantIds;
        }

        public void setVariantIds(List<Long> variantIds) {
            this.variantIds = variantIds;
        }
    }
}
