package com.example.godawari.Models;

public class RecentModel {
    String Profile,Name,Id;

    public RecentModel(String profile, String name, String id) {
        Profile = profile;
        Name = name;
        Id = id;
    }

    public RecentModel(String profile, String name) {
        Profile = profile;
        Name = name;
    }

    public RecentModel() {
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
