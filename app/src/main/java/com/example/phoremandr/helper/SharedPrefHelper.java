package com.example.phoremandr.helper;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    static final String MY_PREF = "RingMemos";
    static  SharedPreferences sharedPreferences;
    static SharedPreferences.Editor myEdit;

    public static void getInstance(Context context){
        sharedPreferences = context.getSharedPreferences(MY_PREF,MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
    }


    public void  setValue(String key, String value){
        myEdit.putString(key, value);
        myEdit.commit();
    }

    public void  setIntValue(String key, int value){
        myEdit.putInt(key, value);
        myEdit.commit();
    }

    public void  setBoolValue(String key, boolean value){
        myEdit.putBoolean(key, value);
        myEdit.commit();
    }


    public String  getValue(String key, String value){
        return  sharedPreferences.getString(key,value);

    }

    public int  getIntValue(String key, int value){
        return sharedPreferences.getInt(key,value);
    }

    public boolean  getBoolValue(String key, boolean value){
        return sharedPreferences.getBoolean(key,value);
    }


    public void clearKey(String key){
        myEdit.remove(key);
        myEdit.commit();
    }

}