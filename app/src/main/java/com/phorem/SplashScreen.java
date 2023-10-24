package com.phorem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.phorem.activities.DashboardActivity;
import com.phorem.activities.SignInScreen;
import com.phorem.base.BaseActivity;
import com.phorem.databinding.ActivitySignupBinding;
import com.phorem.databinding.ActivitySplashBinding;
import com.phorem.receiver.ChatHeadService;
import com.phorem.utils.AppValidator;
import com.phorem.utils.SharedPreferencesKeys;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends BaseActivity {
    ActivitySplashBinding splashBinding;

    @Override
    public ViewBinding getViewModel() {
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

            goToHome();



        Intent intent = new Intent(this , ChatHeadService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);

        FirebaseApp.initializeApp(SplashScreen.this);


        FirebaseMessaging firebaseMessaging =  FirebaseMessaging.getInstance();
        firebaseMessaging.getToken().addOnCompleteListener(task -> {
            AppValidator.logData("getToken","" + task.getResult());
            sharedPrefHelper.setValue(SharedPreferencesKeys.deviceToken, task.getResult());
        });


        return splashBinding;
    }

    @Override
    public void setStatusBarColor(int color) {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));
    }


    public void goToHome() {
        if (!checkDrawOverlayPermission()) {
            new Handler().postDelayed(this::validateUserDetails, 5000);
        }else {
            new Handler().postDelayed(this::validateUserDetails, 2000);
        }


    }

    private static int REQUEST_CODE = 1;
    private boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /** check if we already  have permission to draw over other apps */
            if (!Settings.canDrawOverlays(this)) {
                AppValidator.logData("displayOverLauncher", "canDrawOverlays NOK");
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                displayOverLauncher.launch(intent);;
                return false;
            } else {
                AppValidator.logData("displayOverLauncher", "canDrawOverlays OK");
            }
        }
        return true;
    }

    public void validateUserDetails(){
        try{
            /*SharedPrefHelper.getInstance(SplashScreen.this);
            sharedPrefHelper = new SharedPrefHelper();*/
            if(sharedPrefHelper.getValue(SharedPreferencesKeys.userId) .isEmpty()){
                startActivity(new Intent(SplashScreen.this, SignInScreen.class));
            } else {
                startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
            }
        }catch (NullPointerException e){
           e.printStackTrace();
        }

    }


    ActivityResultLauncher<Intent> displayOverLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    AppValidator.logData("ONDISPLAYLAUNCHER" , "This is your result"+result.getData());

                }
            });


}