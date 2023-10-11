package com.phorem.api_model.get_user_profile;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class GetUserProfileResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private GetUserProfileResponseData data;

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

    public GetUserProfileResponseData getData() {
        return data;
    }

    public void setData(GetUserProfileResponseData data) {
        this.data = data;
    }

}