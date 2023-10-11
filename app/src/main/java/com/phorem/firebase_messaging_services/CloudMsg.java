package com.phorem.firebase_messaging_services;

public class CloudMsg {
    String title, msg,  testValue;
    public CloudMsg(String title, String msg, String testValue) {
        this.msg = msg;
        this.title = title;
        this.testValue = testValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }
}
