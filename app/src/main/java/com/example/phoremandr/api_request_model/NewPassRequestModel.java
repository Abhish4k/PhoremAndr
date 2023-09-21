package com.example.phoremandr.api_request_model;

public class NewPassRequestModel {

    String email , new_password;

    public NewPassRequestModel(String email , String new_password) {
        this.email = email;
        this.new_password = new_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }


}
