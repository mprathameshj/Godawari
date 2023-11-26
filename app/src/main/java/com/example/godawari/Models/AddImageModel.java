package com.example.godawari.Models;

public class AddImageModel {
    private String imageUrl;

    public AddImageModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageId() {
        return getImageUrl();
    }
}
