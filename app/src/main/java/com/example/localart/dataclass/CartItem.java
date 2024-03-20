package com.example.localart.dataclass;

public class CartItem {
    private String productKey;  // Add this field
    private String artName;
    private String artPrice;
    private String artistName;
    private String sellerId;
    private String imageUrl;

    // Required default constructor for Firebase deserialization
    public CartItem() {
        // Default constructor required for calls to DataSnapshot.getValue(CartItem.class)
    }



    public CartItem(String productKey, String artName, String artPrice, String artistName, String sellerId, String imageUrl) {
        this.productKey = productKey;
        this.artName = artName;
        this.artPrice = artPrice;
        this.artistName = artistName;
        this.sellerId = sellerId;
        this.imageUrl = imageUrl;
    }

    // Getter and setter


    public String getArtName() {
        return artName;
    }
    // Add getter and setter for the productKey field
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getArtPrice() {
        return artPrice;
    }

    public void setArtPrice(String artPrice) {
        this.artPrice = artPrice;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
