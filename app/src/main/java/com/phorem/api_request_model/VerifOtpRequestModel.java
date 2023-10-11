package com.phorem.api_request_model;

public class VerifOtpRequestModel {
   String email   , verification_code;;

    public VerifOtpRequestModel(String email , String verification_code) {
        this.email = email;
        this.verification_code = verification_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }








}
