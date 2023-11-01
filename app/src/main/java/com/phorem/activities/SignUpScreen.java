package com.phorem.activities;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.phorem.R;
import com.phorem.api_model.RegisterResponse;
import com.phorem.api_request_model.RegisterRequestModel;
import com.phorem.base.BaseActivity;
import com.phorem.databinding.ActivitySignupBinding;
import com.phorem.utils.AppValidator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpScreen extends BaseActivity {

    ActivitySignupBinding signupBinding;
    @Override
    public ActivitySignupBinding getViewModel() {
        signupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        return signupBinding;
    }

    @Override
    public void setStatusBarColor(int color) {

    }

   public void goToSignIn(View v){
        startActivity(new Intent(SignUpScreen.this, SignInScreen.class));
    }


    public void  onClickSignUpBtn(View v){

        RegisterRequestModel registerRequestModel = new RegisterRequestModel(signupBinding.etFirstName.getText().toString().trim(),signupBinding.etLastName.getText().toString().trim(),signupBinding.etEmail.getText().toString().trim(), signupBinding.etCountry.getText().toString().trim(),
                signupBinding.etPass.getText().toString().trim());

        if(AppValidator.validateRegister(this,registerRequestModel)){
            signupBinding.signUpProgress.setVisibility(View.VISIBLE);
            callRegisterApi(registerRequestModel);
        }

    }

    void  callRegisterApi(RegisterRequestModel registerRequestModel){
        Call<RegisterResponse> call3 = apiInterface.callRegisterApi(registerRequestModel.getFirstName(), registerRequestModel.getLastName(),
                registerRequestModel.getEmail(), registerRequestModel.getCountry(),registerRequestModel.getPassword());

        call3.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NotNull Call<RegisterResponse> call, @NotNull Response<RegisterResponse> response) {
                signupBinding.signUpProgress.setVisibility(View.GONE);
                if(response.body() != null){
                    AppValidator.logData("RESPONSE ON BOTTON CLICK=================>>" , ""+response.body().getCode());
                    if(response.body().getCode().contains("200")){

                        AppValidator.logData("RESPONSE ON BOTTON CLICK=================>>" , ""+response.body().getCode());

                        AppValidator.showToast(SignUpScreen.this, response.body().getMessage());
                        goToSignInPage();
                    }
                }if(response.code() == 400){
                    try {
                        String errorBody = response.errorBody().string();
                        AppValidator.logData("signUpMessages","" + errorBody);

                        JSONObject json = null;

                        try {
                            json = new JSONObject(errorBody);

                            // Extract The User Id From Json Object (With Try Catch)
                            String stringToExtract = null;

                            try {
                                stringToExtract = json.getString("message");
                                AppValidator.showToast(SignUpScreen.this, stringToExtract);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }






            }
            @Override
            public void onFailure(@NotNull  Call<RegisterResponse> call,@NotNull Throwable t) {
                signupBinding.signUpProgress.setVisibility(View.GONE);
                AppValidator.logData("registerError",""+t.getMessage());
            }
        });

    }

    void goToSignInPage(){
        startActivity(new Intent(SignUpScreen.this, SignInScreen.class));
    }
}
