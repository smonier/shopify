package org.jahia.se.modules.shopify.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductFromCsv {
    @JsonProperty("Handle")
    private String handle;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Body (HTML)")
    private String bodyHtml;
    @JsonProperty("Vendor")
    private String vendor;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Tags")
    private String tags;
    @JsonProperty("Published")
    private boolean published;
    @JsonProperty("Option1 Name")
    private String option1Name;
    @JsonProperty("Option1 Value")
    private String option1Value;
    @JsonProperty("Option2 Name")
    private String option2Name;
    @JsonProperty("Option2 Value")
    private String option2Value;
    @JsonProperty("Option3 Name")
    private String option3Name;
    @JsonProperty("Option3 Value")
    private String option3Value;
    @JsonProperty("Variant SKU")
    private String variantSku;
    @JsonProperty("Variant Grams")
    private double variantGrams;
    @JsonProperty("Variant Inventory Tracker")
    private String variantInventoryTracker;
    @JsonProperty("Variant Inventory Qty")
    private int variantInventoryQty;
    @JsonProperty("Variant Inventory Policy")
    private String variantInventoryPolicy;
    @JsonProperty("Variant Fulfillment Service")
    private String variantFulfillmentService;
    @JsonProperty("Variant Price")
    private double variantPrice;
    @JsonProperty("Variant Compare At Price")
    private double variantCompareAtPrice;
    @JsonProperty("Variant Requires Shipping")
    private boolean variantRequiresShipping;
    @JsonProperty("Variant Taxable")
    private boolean variantTaxable;
    @JsonProperty("Variant Barcode")
    private String variantBarcode;
    @JsonProperty("Image Src")
    private String imageSrc;
    @JsonProperty("Image Position")
    private int imagePosition;
    @JsonProperty("Image Alt Text")
    private String imageAltText;
    @JsonProperty("Gift Card")
    private boolean giftCard;
    @JsonProperty("SEO Title")
    private String seoTitle;
    @JsonProperty("SEO Description")
    private String seoDescription;
    @JsonProperty("Variant Image")
    private String variantImage;
    @JsonProperty("Variant Weight Unit")
    private String variantWeightUnit;
    @JsonProperty("Variant Tax Code")
    private String variantTaxCode;

    // Getters and Setters
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getOption1Name() {
        return option1Name;
    }

    public void setOption1Name(String option1Name) {
        this.option1Name = option1Name;
    }

    public String getOption1Value() {
        return option1Value;
    }

    public void setOption1Value(String option1Value) {
        this.option1Value = option1Value;
    }

    public String getOption2Name() {
        return option2Name;
    }

    public void setOption2Name(String option2Name) {
        this.option2Name = option2Name;
    }

    public String getOption2Value() {
        return option2Value;
    }

    public void setOption2Value(String option2Value) {
        this.option2Value = option2Value;
    }

    public String getOption3Name() {
        return option3Name;
    }

    public void setOption3Name(String option3Name) {
        this.option3Name = option3Name;
    }

    public String getOption3Value() {
        return option3Value;
    }

    public void setOption3Value(String option3Value) {
        this.option3Value = option3Value;
    }

    public String getVariantSku() {
        return variantSku;
    }

    public void setVariantSku(String variantSku) {
        this.variantSku = variantSku;
    }

    public double getVariantGrams() {
        return variantGrams;
    }

    public void setVariantGrams(double variantGrams) {
        this.variantGrams = variantGrams;
    }

    public String getVariantInventoryTracker() {
        return variantInventoryTracker;
    }

    public void setVariantInventoryTracker(String variantInventoryTracker) {
        this.variantInventoryTracker = variantInventoryTracker;
    }

    public int getVariantInventoryQty() {
        return variantInventoryQty;
    }

    public void setVariantInventoryQty(int variantInventoryQty) {
        this.variantInventoryQty = variantInventoryQty;
    }

    public String getVariantInventoryPolicy() {
        return variantInventoryPolicy;
    }

    public void setVariantInventoryPolicy(String variantInventoryPolicy) {
        this.variantInventoryPolicy = variantInventoryPolicy;
    }

    public String getVariantFulfillmentService() {
        return variantFulfillmentService;
    }

    public void setVariantFulfillmentService(String variantFulfillmentService) {
        this.variantFulfillmentService = variantFulfillmentService;
    }

    public double getVariantPrice() {
        return variantPrice;
    }

    public void setVariantPrice(double variantPrice) {
        this.variantPrice = variantPrice;
    }

    public double getVariantCompareAtPrice() {
        return variantCompareAtPrice;
    }

    public void setVariantCompareAtPrice(double variantCompareAtPrice) {
        this.variantCompareAtPrice = variantCompareAtPrice;
    }

    public boolean isVariantRequiresShipping() {
        return variantRequiresShipping;
    }

    public void setVariantRequiresShipping(boolean variantRequiresShipping) {
        this.variantRequiresShipping = variantRequiresShipping;
    }

    public boolean isVariantTaxable() {
        return variantTaxable;
    }

    public void setVariantTaxable(boolean variantTaxable) {
        this.variantTaxable = variantTaxable;
    }

    public String getVariantBarcode() {
        return variantBarcode;
    }

    public void setVariantBarcode(String variantBarcode) {
        this.variantBarcode = variantBarcode;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getImagePosition() {
        return imagePosition;
    }

    public void setImagePosition(int imagePosition) {
        this.imagePosition = imagePosition;
    }

    public String getImageAltText() {
        return imageAltText;
    }

    public void setImageAltText(String imageAltText) {
        this.imageAltText = imageAltText;
    }

    public boolean isGiftCard() {
        return giftCard;
    }

    public void setGiftCard(boolean giftCard) {
        this.giftCard = giftCard;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getVariantImage() {
        return variantImage;
    }

    public void setVariantImage(String variantImage) {
        this.variantImage = variantImage;
    }

    public String getVariantWeightUnit() {
        return variantWeightUnit;
    }

    public void setVariantWeightUnit(String variantWeightUnit) {
        this.variantWeightUnit = variantWeightUnit;
    }

    public String getVariantTaxCode() {
        return variantTaxCode;
    }

    public void setVariantTaxCode(String variantTaxCode) {
        this.variantTaxCode = variantTaxCode;
    }
}
