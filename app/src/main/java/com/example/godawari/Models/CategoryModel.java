package com.example.godawari.Models;

public class CategoryModel {
    int Image;
    String id;

    public CategoryModel(int image, String id) {
        Image = image;
        this.id = id;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
