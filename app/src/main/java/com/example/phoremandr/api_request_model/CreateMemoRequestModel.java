package com.example.phoremandr.api_request_model;

public class CreateMemoRequestModel {
    String name, memoName, reminder, phoneNumber, voiceMemo;


    public CreateMemoRequestModel(String name, String memoName, String reminder, String phoneNumber, String voiceMemo) {
        this.name = name;
        this.memoName = memoName;
        this.reminder = reminder;
        this.phoneNumber = phoneNumber;
        this.voiceMemo = voiceMemo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemoName() {
        return memoName;
    }

    public void setMemoName(String memoName) {
        this.memoName = memoName;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVoiceMemo() {
        return voiceMemo;
    }

    public void setVoiceMemo(String voiceMemo) {
        this.voiceMemo = voiceMemo;
    }
}
