package com.example.phoremandr.api_request_model;

public class AddAlarmModel {
    String soundName, sound, channelName;

    public AddAlarmModel(String soundName, String sound, String channelName) {
        this.soundName = soundName;
        this.sound = sound;
        this.channelName = channelName;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
