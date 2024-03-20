package com.example.localart.dataclass;
public class FeaturedProduct {
    private String productName;
    private int productImageResId;

    public FeaturedProduct(String productName, int productImageResId) {
        this.productName = productName;
        this.productImageResId = productImageResId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductImageResId() {
        return productImageResId;
    }
}

