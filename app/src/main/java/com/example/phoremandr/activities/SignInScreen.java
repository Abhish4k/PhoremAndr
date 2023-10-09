package com.example.phoremandr.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.phoremandr.R;
import com.example.phoremandr.SplashScreen;
import com.example.phoremandr.api_model.LoginResponse;
import com.example.phoremandr.api_request_model.LoginRequestModel;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.ActivitySigninBinding;
import com.example.phoremandr.databinding.ActivitySignupBinding;
import com.example.phoremandr.firebase_messaging_services.FirebaseMessageReceiver;
import com.example.phoremandr.utils.AppValidator;
import com.example.phoremandr.utils.SharedPreferencesKeys;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInScreen extends BaseActivity implements View.OnClickListener {


    ActivitySigninBinding signInBinding;

    @Override
    public ActivitySigninBinding getViewModel() {
        signInBinding = DataBindingUtil.setContentView(this, R.layout.activity_signin);

        initView();
        return signInBinding;
    }

    @Override
    public void setStatusBarColor(int color) {

    }


    public void initView() {
        signInBinding.tvSignUp.setOnClickListener(this);
        signInBinding.etPass.setOnClickListener(this);
        signInBinding.etForgot.setOnClickListener(this);
        signInBinding.btnSignIn.setOnClickListener(this);

        checkPermission();
        startPowerSaverIntent(SignInScreen.this);
        FirebaseApp.initializeApp(SignInScreen.this);


        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.getToken().addOnCompleteListener(task -> {
            AppValidator.logData("getFcmToken", "" + task.getResult());
            sharedPrefHelper.setValue(SharedPreferencesKeys.deviceToken, task.getResult());

            AppValidator.logData("LocalFcmmToken", "" + sharedPrefHelper.getValue(SharedPreferencesKeys.deviceToken));

        });

    }

    public void goToSignUp() {
        startActivity(new Intent(SignInScreen.this, SignUpScreen.class));
    }


    private void goToForgotPass() {
        startActivity(new Intent(SignInScreen.this, ForgotPass.class));
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSignUp:
                goToSignUp();
                break;
            case R.id.btnSignIn:
                onClickLoginBtn();
                break;
            case R.id.etForgot:
                goToForgotPass();
                break;
        }
    }


    void onClickLoginBtn() {
        String timeZoneId = TimeZone.getDefault().getID();
        AppValidator.logData("timeZoneID", "=======================>>>>>>" + timeZoneId + "==================>>>>");
        LoginRequestModel loginRequestModel = new LoginRequestModel(signInBinding.etEmail.getText().toString().trim(),
                signInBinding.etPass.getText().toString().trim(), sharedPrefHelper.getValue(SharedPreferencesKeys.deviceToken),
                timeZoneId);
        if (AppValidator.validateLogin(this, loginRequestModel)) {
            signInBinding.loginProgress.setVisibility(View.VISIBLE);
            callLoginApi(loginRequestModel);

        }
    }


    void checkPermission() {
        if (ContextCompat.checkSelfPermission(SignInScreen.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(SignInScreen.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            List<String> listPermissionsNeeded = new ArrayList<>();
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);
            ActivityCompat.requestPermissions(SignInScreen.this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), 101);


        }

    }


    void callLoginApi(LoginRequestModel loginRequestModel) {
        Call<LoginResponse> call3 = apiInterface.callLoginApi(loginRequestModel.getEmail(),
                loginRequestModel.getPassword(),
                sharedPrefHelper.getValue(SharedPreferencesKeys.deviceToken),
                loginRequestModel.getTimeZone());

        call3.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {

                signInBinding.loginProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(SignInScreen.this, response.body().getMessage());
                if (response.body().getCode().contains("200")) {
                    AppValidator.logData("key", "goToDashboard");
                    sharedPrefHelper.setValue(SharedPreferencesKeys.firstName, response.body().getData().getFirstname());
                    sharedPrefHelper.setValue(SharedPreferencesKeys.lastName, response.body().getData().getLastname());
                    sharedPrefHelper.setValue(SharedPreferencesKeys.email, response.body().getData().getEmail());


                    if (!response.body().getData().getToken().isEmpty()) {
                        sharedPrefHelper.setValue(SharedPreferencesKeys.deviceToken, response.body().getData().getToken());
                    }
                    sharedPrefHelper.setValue(SharedPreferencesKeys.userId, response.body().getData().getId());

                    goToDashboard();
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                signInBinding.loginProgress.setVisibility(View.GONE);
                AppValidator.logData("loginProgress", "" + t.getMessage());
            }
        });


    }

    void goToDashboard() {
        FirebaseMessageReceiver firebaseMessageReceiver = new FirebaseMessageReceiver();
        sharedPrefHelper.setValue(SharedPreferencesKeys.channelId, "alarmChannel");
        sharedPrefHelper.setValue(SharedPreferencesKeys.sound, ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.alarm);
        firebaseMessageReceiver.showNotification(SignInScreen.this, getString(R.string.welcome_app), getString(R.string.app_name), sharedPrefHelper.getValue(SharedPreferencesKeys.channelId));

        startActivity(new Intent(SignInScreen.this, DashboardActivity.class));
    }


    public static List<Intent> POWER_MANAGER_INTENTS = Arrays.asList(
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")).setData(android.net.Uri.parse("mobilemanager://function/entry/AutoStart"))
    );


    public static void startPowerSaverIntent(Context context) {
        SharedPreferences settings = context.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE);
        boolean skipMessage = settings.getBoolean("skipProtectedAppCheck", false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            boolean foundCorrectIntent = false;
            for (Intent intent : POWER_MANAGER_INTENTS) {
                if (isCallable(context, intent)) {
                    foundCorrectIntent = true;
                    final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(context);
                    dontShowAgain.setText("Do not show again");
                    dontShowAgain.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        editor.putBoolean("skipProtectedAppCheck", isChecked);
                        editor.apply();
                    });

                    new AlertDialog.Builder(context)
                            .setTitle(Build.MANUFACTURER + " Protected Apps")
                            .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", context.getString(R.string.app_name)))
                            .setView(dontShowAgain)
                            .setPositiveButton("Go to settings", (dialog, which) -> context.startActivity(intent))
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                    break;
                }
            }
            if (!foundCorrectIntent) {
                editor.putBoolean("skipProtectedAppCheck", true);
                editor.apply();
            }
        }
    }



    private static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}
