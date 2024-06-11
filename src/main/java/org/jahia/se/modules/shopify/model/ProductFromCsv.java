package org.jahia.se.modules.shopify.model;

import java.util.List;

public class ProductFromCsv {
    private String handle;
    private String title;
    private String bodyHtml;
    private String vendor;
    private String type;
    private String tags;
    private boolean published;
    private String option1Name;
    private String option1Value;
    private String option2Name;
    private String option2Value;
    private String option3Name;
    private String option3Value;
    private String variantSku;
    private int variantGrams;
    private String variantInventoryTracker;
    private int variantInventoryQty;
    private String variantInventoryPolicy;
    private String variantFulfillmentService;
    private double variantPrice;
    private double variantCompareAtPrice;
    private boolean variantRequiresShipping;
    private boolean variantTaxable;
    private String variantBarcode;
    private String imageSrc;
    private int imagePosition;
    private String imageAltText;
    private boolean giftCard;
    private String seoTitle;
    private String seoDescription;
    private String variantImage;
    private String variantWeightUnit;
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

    public int getVariantGrams() {
        return variantGrams;
    }

    public void setVariantGrams(int variantGrams) {
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
