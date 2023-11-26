package com.example.godawari.Models;

public class BannerModel {
    private String Image, Link;

    public BannerModel(String image, String link) {
        Image = image;
        Link = link;
    }

    public BannerModel() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
