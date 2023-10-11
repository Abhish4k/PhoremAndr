package com.phorem.activities;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.phorem.R;
import com.phorem.api_model.RegisterResponse;
import com.phorem.api_request_model.RegisterRequestModel;
import com.phorem.base.BaseActivity;
import com.phorem.databinding.ActivitySignupBinding;
import com.phorem.utils.AppValidator;

import org.jetbrains.annotations.NotNull;

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
                registerRequestModel.getEmail(),registerRequestModel.getPassword(), registerRequestModel.getCountry());

        call3.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NotNull Call<RegisterResponse> call, @NotNull Response<RegisterResponse> response) {

                signupBinding.signUpProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(SignUpScreen.this, response.body().getMessage());
                if(response.body().getCode().contains("200")){

                    goToSignInPage();
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
