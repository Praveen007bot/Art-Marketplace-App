package com.example.localart.dataclass;

// Address.java
// Address.java
// Address.java
public class Address {

    private String name;
    private String phone;
    private String houseNumber;
    private String city;
    private String state;
    private String pincode;

    public Address() {
        // Default constructor required for Firebase
    }

    public Address(String name, String phone, String houseNumber, String city, String state, String pincode) {
        this.name = name;
        this.phone = phone;
        this.houseNumber = houseNumber;

        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

    // Getter and setter for street




    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter and setter for houseNumber
    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    // Getter and setter for city
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // Getter and setter for state
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Getter and setter for pincode
    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
