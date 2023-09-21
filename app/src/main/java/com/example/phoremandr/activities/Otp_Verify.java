package com.example.phoremandr.activities;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.example.phoremandr.R;
import com.example.phoremandr.api_model.OtpVerifResponse;
import com.example.phoremandr.api_request_model.VerifOtpRequestModel;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.OtpVerifBinding;
import com.example.phoremandr.utils.AppValidator;

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
        callOtpVerifyApi(verifOtpRequestModel);
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

                assert response.body() != null;
                AppValidator.showToast(Otp_Verify.this, response.body().getMessage());
                if(response.body().getCode().contains("200")){
                    goToCreatePassScreen(verifOtpRequestModel.getEmail().trim());

                }

            }
            @Override
            public void onFailure(@NotNull  Call<OtpVerifResponse> call,@NotNull Throwable t) {
                otpVerifBinding.verifyProgress.setVisibility(View.GONE);
                AppValidator.logData("otpVerificationError",""+t.getMessage());
            }
        });

    }

    void goToCreatePassScreen(String email){
        Intent intent = new Intent(Otp_Verify.this , CreateNewPass.class);
        intent.putExtra("ConfirmedEmail", email);
        startActivity(intent);
    }




}
