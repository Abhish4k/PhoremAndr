package com.example.phoremandr.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.ActivitySignupBinding;

public class SignUpScreen extends BaseActivity implements View.OnClickListener {

    ActivitySignupBinding signupBinding;
    @Override
    public ViewBinding getViewModel() {
        signupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        initView();
        return signupBinding;
    }

    @Override
    public void setStatusBarColor(int color) {

    }


    void initView(){
        signupBinding.btnSignUp.setOnClickListener(this);
        signupBinding.tvSignIn.setOnClickListener(this);
    }


    void goToSignIn(){
        startActivity(new Intent(SignUpScreen.this, SignInScreen.class));
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSignUp:

                break;
            case R.id.tvSignIn:
                goToSignIn();
                break;
        }
    }
}
