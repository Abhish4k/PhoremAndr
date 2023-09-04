package com.example.phoremandr.api_model.add_alarm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAlarmRequestDataModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("channel_id")
    @Expose
    private String channelId;
    @SerializedName("custom_sound")
    @Expose
    private String customSound;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCustomSound() {
        return customSound;
    }

    public void setCustomSound(String customSound) {
        this.customSound = customSound;
    }

}
