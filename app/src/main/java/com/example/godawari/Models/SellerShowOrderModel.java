package com.example.godawari.Models;

public class SellerShowOrderModel {
    String Product,Ram,Colour,Date,Status,OrderId;

    public SellerShowOrderModel(String product, String ram, String colour, String date, String status, String orderId) {
        Product = product;
        Ram = ram;
        Colour = colour;
        Date = date;
        Status = status;
        OrderId = orderId;
    }

    public SellerShowOrderModel() {
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getRam() {
        return Ram;
    }

    public void setRam(String ram) {
        Ram = ram;
    }

    public String getColour() {
        return Colour;
    }

    public void setColour(String colour) {
        Colour = colour;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
