package com.example.phoremandr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.activities.DashboardActivity;
import com.example.phoremandr.activities.SignInScreen;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.ActivitySplashBinding;
import com.example.phoremandr.firebase_messaging_services.FirebaseMessageReceiver;
import com.example.phoremandr.receiver.ChatHeadService;
import com.example.phoremandr.utils.AppValidator;
import com.example.phoremandr.utils.SharedPreferencesKeys;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends BaseActivity {
    ActivitySplashBinding splashBinding;

    @Override
    public ViewBinding getViewModel() {

        splashBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash);





        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            displayOverLauncher.launch(intent);
        }

        checkPermission();
        goToHome();


        Intent intent = new Intent(this , ChatHeadService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);

        FirebaseApp.initializeApp(SplashScreen.this);



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


    ActivityResultLauncher<Intent> displayOverLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();

                }
            });

    void checkPermission(){
        if(ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ){
            List<String> listPermissionsNeeded = new ArrayList<>();
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            ActivityCompat.requestPermissions(SplashScreen.this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),101);

        }

    }



}