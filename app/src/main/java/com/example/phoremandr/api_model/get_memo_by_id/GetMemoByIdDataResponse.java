package com.example.phoremandr.api_model.get_memo_by_id;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMemoByIdDataResponse {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("memo")
    @Expose
    private String memo;
    @SerializedName("voice_memo")
    @Expose
    private String voiceMemo;
    @SerializedName("reminder")
    @Expose
    private String reminder;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getVoiceMemo() {
        return voiceMemo;
    }

    public void setVoiceMemo(String voiceMemo) {
        this.voiceMemo = voiceMemo;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

}
