package com.example.phoremandr.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.api_model.LoginResponse;
import com.example.phoremandr.api_model.LoginResponse;
import com.example.phoremandr.api_model.LoginResponseData;
import com.example.phoremandr.api_request_model.LoginRequestModel;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.ActivitySigninBinding;
import com.example.phoremandr.utils.AppValidator;
import com.example.phoremandr.utils.SharedPreferencesKeys;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInScreen extends BaseActivity implements View.OnClickListener {



    ActivitySigninBinding signInBinding;
    @Override
    public ViewBinding getViewModel() {
        signInBinding = DataBindingUtil.setContentView(this, R.layout.activity_signin);

        initView();
        return signInBinding;
    }

    @Override
    public void setStatusBarColor(int color) {

    }


    public void  initView(){
        signInBinding.tvSignUp.setOnClickListener(this);
        signInBinding.etPass.setOnClickListener(this);
        signInBinding.btnSignIn.setOnClickListener(this);
    }

    private  void goToSignUp(){
        startActivity(new Intent(SignInScreen.this, SignUpScreen.class));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSignUp:
                goToSignUp();
                break;
            case R.id.btnSignIn:
                onClickLoginBtn();
                break;

            case R.id.etPass:
                if(signInBinding.etPass.getTransformationMethod() == PasswordTransformationMethod.getInstance()){
                    signInBinding.etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    signInBinding.etPass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.eye_off,0);
                } else{
                    signInBinding.etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    signInBinding.etPass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.eye,0);
                }
                break;
        }

    }


    void  onClickLoginBtn(){
        LoginRequestModel loginRequestModel = new LoginRequestModel(signInBinding.etEmail.getText().toString().trim(),signInBinding.etPass.getText().toString().trim(), "");
        if(AppValidator.validateLogin(this,loginRequestModel)){
            signInBinding.loginProgress.setVisibility(View.VISIBLE);
            callLoginApi(loginRequestModel);
        }

    }

    void  callLoginApi(LoginRequestModel loginRequestModel){
        Call<LoginResponse> call3 = apiInterface.callLoginApi(loginRequestModel.getEmail(),loginRequestModel.getPassword(), loginRequestModel.getDeviceToken());

        call3.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull  Response<LoginResponse> response) {

                signInBinding.loginProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(SignInScreen.this, response.body().getMessage());
                if(response.body().getCode().contains("200")){
                    AppValidator.logData("key","goToDashboard");
                    sharedPrefHelper.setValue(SharedPreferencesKeys.firstName,response.body().getData().getFirstname());
                    sharedPrefHelper.setValue(SharedPreferencesKeys.lastName, response.body().getData().getLastname());
                    sharedPrefHelper.setValue(SharedPreferencesKeys.email, response.body().getData().getEmail());

                    if(!response.body().getData().getToken().isEmpty()){
                        sharedPrefHelper.setValue(SharedPreferencesKeys.deviceToken, response.body().getData().getToken());
                    }
                    sharedPrefHelper.setValue(SharedPreferencesKeys.userId, response.body().getData().getId());

                    goToDashboard();
                }


            }
            @Override
            public void onFailure(@NotNull  Call<LoginResponse> call,@NotNull Throwable t) {
                signInBinding.loginProgress.setVisibility(View.GONE);
                AppValidator.logData("loginProgress",""+t.getMessage());
            }
        });



          }

    void goToDashboard(){

        startActivity(new Intent(SignInScreen.this, DashboardActivity.class));
    }



}
