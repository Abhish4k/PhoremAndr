package com.example.phoremandr.api_request_model;

public class AddAlarmModel {
    String soundName, sound;

    public AddAlarmModel(String soundName, String sound) {
        this.soundName = soundName;
        this.sound = sound;
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
}
