package com.example.phoremandr.activities;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import com.example.phoremandr.R;
import com.example.phoremandr.api_model.ForgetPassResponse;
import com.example.phoremandr.api_request_model.ForgetPassRequestModel;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.ForgetPasswordBinding;
import com.example.phoremandr.utils.AppValidator;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPass extends BaseActivity {

     ForgetPasswordBinding forgetPassBinding ;


    @Override
    public ForgetPasswordBinding getViewModel() {
        forgetPassBinding = DataBindingUtil.setContentView(ForgotPass.this, R.layout.forget_password );

        return forgetPassBinding;
    }

    @Override
    public void setStatusBarColor(int color) {

    }


    public void goToVerify(View v) {
        ForgetPassRequestModel forgetPassRequestModel = new ForgetPassRequestModel(forgetPassBinding.forgetEmail.getText().toString().trim());
        callForgotPasswordApi(forgetPassRequestModel);
        if (AppValidator.validateForgetPass(this , forgetPassRequestModel)){
            forgetPassBinding.forgotProgress.setVisibility(View.VISIBLE);
            callForgotPasswordApi(forgetPassRequestModel);
        }
    }


    public void goToSignInScreen(View v) {
        startActivity(new Intent(ForgotPass.this, SignInScreen.class));
    }



    void callForgotPasswordApi (ForgetPassRequestModel forgetPassRequestModel){
        Call<ForgetPassResponse> call3 = apiInterface.callForgotPasswordApi(forgetPassRequestModel.getEmail());

        call3.enqueue(new Callback<ForgetPassResponse>() {
            @Override
            public void onResponse(@NotNull Call<ForgetPassResponse> call, @NotNull Response<ForgetPassResponse> response) {
              forgetPassBinding.forgotProgress.setVisibility(View.GONE);

                assert response.body() != null;
                 AppValidator.showToast(ForgotPass.this, response.body().getMessage());
                    if (response.body().getCode().contains("200")){
                     goToVerifyPage(forgetPassRequestModel.getEmail());
                    }

            }

            @Override
            public void onFailure(@NotNull Call<ForgetPassResponse> call,@NotNull Throwable t) {
                forgetPassBinding.forgotProgress.setVisibility(View.GONE);
                AppValidator.logData("forgetPassError",""+t.getMessage());
            }

        });
    }

    void goToVerifyPage(String userEmail){
        Intent intent = new Intent(ForgotPass.this , Otp_Verify.class);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);

    }



}
