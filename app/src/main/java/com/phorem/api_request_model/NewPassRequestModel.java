package com.phorem.api_request_model;

public class NewPassRequestModel {

    String email , new_password , conf_Pass;

    public NewPassRequestModel(String email , String new_password, String conf_Pass) {
        this.email = email;
        this.new_password = new_password;
        this.conf_Pass = conf_Pass;

    }

    public String getConf_Pass() {
        return conf_Pass;
    }

    public void setConf_Pass(String conf_Pass) {
        this.conf_Pass = conf_Pass;
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
