package com.example.localart.dataclass;

public class OrderItem {
    private String orderId;
    private String productKey;
    private String artName;
    private String artPrice;
    private String artistName;
    private String imageUrl;
    private String sellerId;

    // Default constructor required for Firebase
    public OrderItem() {
    }

    public OrderItem(String orderId, String productKey, String artName, String artPrice, String artistName, String imageUrl, String sellerId) {
        this.orderId = orderId;
        this.productKey = productKey;
        this.artName = artName;
        this.artPrice = artPrice;
        this.artistName = artistName;
        this.imageUrl = imageUrl;
        this.sellerId = sellerId;
    }

    // Getter methods
    public String getOrderId() {
        return orderId;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getArtName() {
        return artName;
    }

    public String getArtPrice() {
        return artPrice;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSellerId() {
        return sellerId;
    }

    // Setter methods
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public void setArtPrice(String artPrice) {
        this.artPrice = artPrice;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
