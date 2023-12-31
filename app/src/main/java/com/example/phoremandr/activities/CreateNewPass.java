package com.example.phoremandr.activities;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.api_model.NewPassResponse;
import com.example.phoremandr.api_model.OtpVerifResponse;
import com.example.phoremandr.api_request_model.NewPassRequestModel;
import com.example.phoremandr.api_request_model.VerifOtpRequestModel;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.CreatenewpassBinding;
import com.example.phoremandr.utils.AppValidator;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewPass extends BaseActivity {

    CreatenewpassBinding createnewpassbinding;
    @Override
    public CreatenewpassBinding getViewModel() {

        createnewpassbinding = DataBindingUtil.setContentView(CreateNewPass.this, R.layout.createnewpass );
           initView();
        return createnewpassbinding;

    }


    private void initView() {
        String confirmedEmail = getIntent().getStringExtra("ConfirmedEmail");
        TextView emailTextView = findViewById(R.id.etConfEmail);

        emailTextView.setText(confirmedEmail);
    }

    @Override
    public void setStatusBarColor(int color) {

    }

    public void onClickResetPassBtn(View v){
        NewPassRequestModel newPassRequestModel = new NewPassRequestModel(
                createnewpassbinding.etConfEmail.getText().toString().trim() ,
                createnewpassbinding.etNewPass.getText().toString().trim() ,
                createnewpassbinding.etConfPass.getText().toString().trim());
        if (AppValidator.validateResetPass(CreateNewPass.this , newPassRequestModel)){
            createnewpassbinding.resetPassProgress.setVisibility(View.VISIBLE);
            callResetPassApi(newPassRequestModel);
        }
    }




    void  callResetPassApi(NewPassRequestModel newPassRequestModel){
        if (newPassRequestModel.getConf_Pass().equals(newPassRequestModel.getNew_password())) {
            Call<NewPassResponse> call3 = apiInterface.callResetPassApi(newPassRequestModel.getEmail(), newPassRequestModel.getNew_password());

            call3.enqueue(new Callback<NewPassResponse>() {
                @Override
                public void onResponse(@NotNull Call<NewPassResponse> call, @NotNull Response<NewPassResponse> response) {
                    createnewpassbinding.resetPassProgress.setVisibility(View.GONE);

                    assert response.body() != null;
                    AppValidator.showToast(CreateNewPass.this, response.body().getMessage());
                    if(response.body().getCode().contains("200")){

                        goToSignInScreen();

                    }

                }
                @Override
                public void onFailure(@NotNull  Call<NewPassResponse> call,@NotNull Throwable t) {
                    createnewpassbinding.resetPassProgress.setVisibility(View.GONE);
                    AppValidator.logData("otpVerificationError",""+t.getMessage());
                }
            });
        }



    }

    void goToSignInScreen(){
      startActivity(new Intent(CreateNewPass.this , SignInScreen.class));
    }


}
