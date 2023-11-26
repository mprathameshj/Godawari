package com.example.godawari.Models;

public class SellerShowProductModel {
    String Name,Price,Profile,Id;

    public SellerShowProductModel(String name, String price, String profile, String id) {
        Name = name;
        Price = price;
        Profile = profile;
        Id = id;
    }

    public SellerShowProductModel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
