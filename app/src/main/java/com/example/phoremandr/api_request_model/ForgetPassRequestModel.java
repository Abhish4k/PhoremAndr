package com.example.phoremandr.api_request_model;

public class ForgetPassRequestModel {

    String email;

    public ForgetPassRequestModel(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
