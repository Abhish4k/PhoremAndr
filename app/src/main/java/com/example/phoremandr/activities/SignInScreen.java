package com.example.phoremandr.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.phoremandr.R;
import com.example.phoremandr.api_model.LoginResponse;
import com.example.phoremandr.api_request_model.LoginRequestModel;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.ActivitySigninBinding;
import com.example.phoremandr.databinding.ActivitySignupBinding;
import com.example.phoremandr.utils.AppValidator;
import com.example.phoremandr.utils.SharedPreferencesKeys;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

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
            case R.id.etForgot :
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

    void callLoginApi(LoginRequestModel loginRequestModel) {
        Call<LoginResponse> call3 = apiInterface.callLoginApi(loginRequestModel.getEmail(), loginRequestModel.getPassword(),  sharedPrefHelper.getValue(SharedPreferencesKeys.deviceToken), loginRequestModel.getTimeZone());

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
                Log.d("Device Token", "This is device token=================>>" + response.body().getData().getToken());


            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                signInBinding.loginProgress.setVisibility(View.GONE);
                AppValidator.logData("loginProgress", "" + t.getMessage());
            }
        });


    }

    void goToDashboard() {

        startActivity(new Intent(SignInScreen.this, DashboardActivity.class));
    }


}
