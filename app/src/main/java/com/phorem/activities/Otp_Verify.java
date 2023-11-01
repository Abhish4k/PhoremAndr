package com.phorem.activities;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.phorem.R;
import com.phorem.api_model.ForgetPassResponse;
import com.phorem.api_model.OtpVerifResponse;
import com.phorem.api_request_model.ForgetPassRequestModel;
import com.phorem.api_request_model.VerifOtpRequestModel;
import com.phorem.base.BaseActivity;
import com.phorem.databinding.OtpVerifBinding;
import com.phorem.utils.AppValidator;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Otp_Verify extends BaseActivity {
    OtpVerifBinding otpVerifBinding;
    @Override
    public OtpVerifBinding getViewModel() {
        otpVerifBinding = DataBindingUtil.setContentView(Otp_Verify.this, R.layout.otp_verif );
         initView();
        return otpVerifBinding;
    }


    private void initView() {
        TextView resendOtp = findViewById(R.id.ResendOtpBtn);
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        TextView emailTextView = findViewById(R.id.verifEmail);

        emailTextView.setText(userEmail);
    }

    @Override
    public void setStatusBarColor(int color) {

    }


    public  void onClickVerifyBtn(View v){
        VerifOtpRequestModel verifOtpRequestModel = new VerifOtpRequestModel(otpVerifBinding.verifEmail.getText().toString().trim() ,
                otpVerifBinding.pinview.getText().toString().trim());
        if (AppValidator.validateOtpVerify(Otp_Verify.this , verifOtpRequestModel)){
            otpVerifBinding.verifyProgress.setVisibility(View.VISIBLE);
            callOtpVerifyApi(verifOtpRequestModel);
        }
    }


    void  callOtpVerifyApi(VerifOtpRequestModel verifOtpRequestModel){
        Call<OtpVerifResponse> call3 = apiInterface.callOtpVerifyApi(verifOtpRequestModel.getEmail(), verifOtpRequestModel.getVerification_code());

        call3.enqueue(new Callback<OtpVerifResponse>() {
            @Override
            public void onResponse(@NotNull Call<OtpVerifResponse> call, @NotNull Response<OtpVerifResponse> response) {
                otpVerifBinding.verifyProgress.setVisibility(View.GONE);
                if (response.body() != null){
                    AppValidator.showToast(Otp_Verify.this, response.body().getMessage());
                    if(response.body().getCode().contains("200")){
                        goToCreatePassScreen(verifOtpRequestModel.getEmail().trim());

                    }
                }


            }
            @Override
            public void onFailure(@NotNull  Call<OtpVerifResponse> call,@NotNull Throwable t) {
                otpVerifBinding.verifyProgress.setVisibility(View.GONE);
                AppValidator.logData("otpVerificationError",""+t.getMessage());
            }
        });

    }


    public void onClickResendBtn(View v){
        ForgetPassRequestModel forgetPassRequestModel = new ForgetPassRequestModel(otpVerifBinding.verifEmail.getText().toString().trim() );
        if (AppValidator.validateForgetPass(this , forgetPassRequestModel)){
            otpVerifBinding.verifyProgress.setVisibility(View.VISIBLE);
            callForgotPasswordApi(forgetPassRequestModel);
        }


    }

    void callForgotPasswordApi (ForgetPassRequestModel forgetPassRequestModel){
        Call<ForgetPassResponse> call3 = apiInterface.callForgotPasswordApi(forgetPassRequestModel.getEmail());

        call3.enqueue(new Callback<ForgetPassResponse>() {
            @Override
            public void onResponse(@NotNull Call<ForgetPassResponse> call, @NotNull Response<ForgetPassResponse> response) {

                if (response.body() != null){
                    AppValidator.logData("RESPONSEEEEEEE=================", ""+response.body().getMessage());
                    AppValidator.showToast(Otp_Verify.this, response.body().getMessage());
                    if (response.body().getCode().contains("200")){
                        forgetPassRequestModel.getEmail();

                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<ForgetPassResponse> call,@NotNull Throwable t) {
                AppValidator.logData("forgetPassError",""+t.getMessage());
            }

        });
    }





    void goToCreatePassScreen(String email){
        Intent intent = new Intent(Otp_Verify.this , CreateNewPass.class);
        intent.putExtra("ConfirmedEmail", email);
        startActivity(intent);
    }


}
