package com.example.godawari.Models;

public class MyOrdersModel {
    String Profile,Name,Status,OrderId;

    public MyOrdersModel(String profile, String name, String status, String orderId) {
        Profile = profile;
        Name = name;
        Status = status;
        OrderId = orderId;
    }

    public MyOrdersModel() {
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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
