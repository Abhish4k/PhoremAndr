package com.phorem.helper;

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


    public String  getValue(String key){
        return  sharedPreferences.getString(key,"");

    }

    public int  getIntValue(String key){
        return sharedPreferences.getInt(key,0);
    }

    public boolean  getBoolValue(String key){
        return sharedPreferences.getBoolean(key,false);
    }


    public void clearKey(String key){
        myEdit.remove(key);
        myEdit.commit();
    }

    public void erseAllData(){
        myEdit.clear();
        myEdit.commit();
    }

}