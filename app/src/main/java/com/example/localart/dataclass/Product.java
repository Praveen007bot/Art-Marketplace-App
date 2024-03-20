package com.example.localart.dataclass;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.localart.dataclass.CartItem;
import com.google.firebase.database.PropertyName;

public class Product implements Parcelable {
    @PropertyName("artName")
    private String artName;

    @PropertyName("artPrice")
    private String artPrice;

    @PropertyName("imageUrl")
    private String imageUrl;

    @PropertyName("artistName")
    private String artistName;

    @PropertyName("sellerId")
    private String sellerId;

    @PropertyName("productKey")
    private String productKey;

    // No-argument constructor required by Firebase
    public Product() {
        // Default constructor required by Firebase
    }

    // Existing methods...
    public Product(CartItem cartItem) {
        this.artName = cartItem.getArtName();
        this.artistName = cartItem.getArtistName();
        this.artPrice = cartItem.getArtPrice();
    }

    protected Product(Parcel in) {
        // Read data from parcel
        // Ensure that you read the data in the same order it was written
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product(Product product) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write data to parcel
        // Example: dest.writeString(name);
    }

    // Setter methods
    public void setArtName(String artName) {
        this.artName = artName;
    }

    public void setArtPrice(String artPrice) {
        this.artPrice = artPrice;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    // Getter methods
    public String getArtName() {
        return artName;
    }

    public String getArtPrice() {
        return artPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getProductKey() {
        return productKey;
    }
}
