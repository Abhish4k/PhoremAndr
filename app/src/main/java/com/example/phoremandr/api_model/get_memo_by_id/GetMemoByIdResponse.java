package com.example.phoremandr.api_model.get_memo_by_id;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetMemoByIdResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private GetMemoByIdDataResponse data;

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

    public GetMemoByIdDataResponse getData() {
        return data;
    }

    public void setData(GetMemoByIdDataResponse data) {
        this.data = data;
    }

}