package com.example.phoremandr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.activities.DashboardActivity;
import com.example.phoremandr.activities.SignInScreen;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.ActivitySplashBinding;
import com.example.phoremandr.receiver.ChatHeadService;
import com.example.phoremandr.utils.AppValidator;
import com.example.phoremandr.utils.SharedPreferencesKeys;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends BaseActivity {
    ActivitySplashBinding splashBinding;

    @Override
    public ViewBinding getViewModel() {

        splashBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash);

        if(ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[]{ Manifest.permission.READ_PHONE_STATE},100);
        }

        goToHome();


        Intent intent = new Intent(this , ChatHeadService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);

        return splashBinding;
    }

    @Override
    public void setStatusBarColor(int color) {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));
    }


    public  void goToHome(){
        new Handler().postDelayed(this::validateUserDetails, 2000);
    }


    public void validateUserDetails(){
        AppValidator.logData("userId","" + sharedPrefHelper.getValue(SharedPreferencesKeys.userId));
        if(sharedPrefHelper.getValue(SharedPreferencesKeys.userId).isEmpty()){
            startActivity(new Intent(SplashScreen.this, SignInScreen.class));
        }else {
            startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
        }
    }




}