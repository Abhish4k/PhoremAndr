package com.example.phoremandr.api_model.add_alarm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAlarmRequestModel {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private AddAlarmRequestDataModel data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AddAlarmRequestDataModel getData() {
        return data;
    }

    public void setData(AddAlarmRequestDataModel data) {
        this.data = data;
    }

}

