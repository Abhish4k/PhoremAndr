package com.phorem.api_request_model;

public class LoginRequestModel {
    String email, password, deviceToken, timeZone ;


    public LoginRequestModel(String email, String password, String deviceToken, String timeZone ) {
        this.email = email;
        this.password = password;
        this.deviceToken = deviceToken;
        this.timeZone = timeZone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
