package com.example.godawari.Models;

public class ChangeAddressModel {

    String Name,DetailAddress,Pincode,State,City,Id,MobileNumber;

    public ChangeAddressModel(String name, String detailAddress, String pincode, String state, String city, String id, String mobileNumber) {
        Name = name;
        DetailAddress = detailAddress;
        Pincode = pincode;
        State = state;
        City = city;
        Id = id;
        MobileNumber = mobileNumber;
    }

    public ChangeAddressModel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDetailAddress() {
        return DetailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        DetailAddress = detailAddress;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }
}
