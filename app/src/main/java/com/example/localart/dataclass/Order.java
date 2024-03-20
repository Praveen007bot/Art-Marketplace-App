package com.example.localart.dataclass;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private double amount;
    private String placedOn;
    private String userId;
    private Address shippingAddress;
    private List<OrderItem> orderItems;
    private transient NonSerializableClass nonSerializableField;

    public Order() {
    }

    public Order(String orderId, double amount, String userId, String placedOn, Address shippingAddress) {
        this.orderId = orderId;
        this.amount = amount;
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.placedOn = placedOn;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public NonSerializableClass getNonSerializableField() {
        return nonSerializableField;
    }

    public void setNonSerializableField(NonSerializableClass nonSerializableField) {
        this.nonSerializableField = nonSerializableField;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and setter for shippingAddress
    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address selectedAddress) {
        this.shippingAddress = selectedAddress;
    }

    public String getPlacedOn() {
        return placedOn;
    }

    public void setPlacedOn(String placedOn) {
        this.placedOn = placedOn;
    }

    // Getter and setter for products

    // Add other getters and setters as needed
}
