package com.example.godawari.Models;

public class SearchModel {
    String Name,Rating,RamDefault,Battery,Display,RetailPrice,Price,Profile,Camera,Discount,SelfieCamera,Note,
            Discription,Id;

    public SearchModel(String name, String rating, String ramDefault, String battery, String display, String retailPrice, String price, String profile, String camera, String discount,String selfieCamera,String note,String discription,String id) {
        Name = name;
        Rating = rating;
        RamDefault = ramDefault;
        Battery = battery;
        Display = display;
        RetailPrice = retailPrice;
        Price = price;
        Profile = profile;
        Camera = camera;
        Discount = discount;
        Id=id;
        SelfieCamera=selfieCamera;
        Note=note;
        Discription=discription;
    }

    public SearchModel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getId() {
        return Id;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSelfieCamera() {
        return SelfieCamera;
    }

    public void setSelfieCamera(String selfieCamera) {
        SelfieCamera = selfieCamera;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getRamDefault() {
        return RamDefault;
    }

    public void setRamDefault(String ramDefault) {
        RamDefault = ramDefault;
    }

    public String getBattery() {
        return Battery;
    }

    public void setBattery(String battery) {
        Battery = battery;
    }

    public String getDisplay() {
        return Display;
    }

    public void setDisplay(String display) {
        Display = display;
    }

    public String getRetailPrice() {
        return RetailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        RetailPrice = retailPrice;
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

    public String getCamera() {
        return Camera;
    }

    public void setCamera(String camera) {
        Camera = camera;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
