package com.example.phoremandr.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.phoremandr.R;
import com.example.phoremandr.activities.SignInScreen;
import com.example.phoremandr.api_request_model.CreateMemoRequestModel;
import com.example.phoremandr.api_request_model.LoginRequestModel;
import com.example.phoremandr.api_request_model.RegisterRequestModel;
import com.example.phoremandr.api_request_model.UpdateProfileRequestModel;
import com.example.phoremandr.helper.SharedPrefHelper;

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



    public static  boolean validateRegister(Context context, RegisterRequestModel registerRequestModel){

        if(registerRequestModel.getFirstName().isEmpty()){
            showToast(context, context.getString(R.string.enter_first_name));
            return  false;
        }

        if(registerRequestModel.getLastName().isEmpty()){
            showToast(context, context.getString(R.string.enter_last_name));
            return  false;
        }

        if (registerRequestModel.getEmail().isEmpty()){
            showToast(context,context.getString(R.string.enter_email));
            return  false;
        }

        if(!isValid(registerRequestModel.getEmail())){
            showToast(context,context.getString(R.string.enter_valid_email));
            return  false;
        }

        if(registerRequestModel.getCountry().isEmpty()){
            showToast(context, context.getString(R.string.enter_country));
            return  false;
        }

        if(registerRequestModel.getPassword().isEmpty()){
            showToast(context, context.getString(R.string.enter_password));
            return false;
        }

        return  true;
    }


    public  static  boolean validateProfile(Context context,UpdateProfileRequestModel updateProfileRequestModel){

         if (updateProfileRequestModel.getFirstName().isEmpty()){
             showToast(context, context.getString(R.string.enter_first_name));
             return false;
         }

         if (updateProfileRequestModel.getLastName().isEmpty()){
             showToast(context, context.getString(R.string.enter_last_name));
             return false;
         }


         if (updateProfileRequestModel.getEmail().isEmpty()){
             showToast(context, context.getString(R.string.enter_email));
             return false;
         }

        if(!isValid(updateProfileRequestModel.getEmail())){
            showToast(context,context.getString(R.string.enter_valid_email));
            return  false;
        }

        return true;
    }


    public  static    void showLogoutPopup(Context context, SharedPrefHelper sharedPrefHelper) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.logout));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(context.getString(R.string.want_to_logout));
        builder.setPositiveButton(context.getString(R.string.logout), (dialog, which) -> {
            sharedPrefHelper.erseAllData();
            context.startActivity(new Intent(context, SignInScreen.class));

        });
        builder.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
      /*  int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        alertDialog.getWindow().setType(LAYOUT_FLAG);*/
        alertDialog.show();
    }


    public static boolean validateCreateMemo(Context context,CreateMemoRequestModel createMemoRequestModel){

         if(createMemoRequestModel.getName().isEmpty()){
             showToast(context, context.getString(R.string.enter_name));
             return false;
         }

         if(createMemoRequestModel.getPhoneNumber().isEmpty()){
             showToast(context, context.getString(R.string.enter_phone_number));
             return false;
         }

         if(createMemoRequestModel.getMemoName().isEmpty()){
             showToast(context, context.getString(R.string.enter_memo_name));
             return  false;
         }

         if(createMemoRequestModel.getReminder().isEmpty() || createMemoRequestModel.getReminder().contains(context.getString(R.string.set_reminder))){
             showToast(context, context.getString(R.string.select_reminder));
             return  false;
         }

         return  true;
    }
}
