package com.example.phoremandr.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.phoremandr.R;
import com.example.phoremandr.api_request_model.LoginRequestModel;

import java.util.regex.Pattern;

public class AppValidator {

     public  static void  showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void  logData(String key, String message){
        Log.e(key,message);
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static  boolean validateLogin(Context context,LoginRequestModel loginRequestModel){

        if (loginRequestModel.getEmail().isEmpty()){
            showToast(context,context.getString(R.string.enter_email));
            return  false;
        }

        if(!isValid(loginRequestModel.getEmail())){
            showToast(context,context.getString(R.string.enter_valid_email));
            return  false;
        }

        if(loginRequestModel.getPassword().isEmpty()){
            showToast(context, context.getString(R.string.enter_password));
            return false;
        }

        return  true;
    }
}
