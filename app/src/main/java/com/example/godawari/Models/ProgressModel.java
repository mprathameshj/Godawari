package com.example.godawari.Models;

public class ProgressModel {

    String Status;

    public ProgressModel(String status) {
        Status = status;
    }

    public ProgressModel() {
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
