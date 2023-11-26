package com.example.godawari.Models;

public class PreSearchModel {
    String Name,Profile;

    public PreSearchModel() {
    }

    public PreSearchModel(String name, String profile) {
        Name = name;
        Profile = profile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }
}
