package com.example.phoremandr.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import com.example.phoremandr.R;
import com.example.phoremandr.api_services.APIClient;
import com.example.phoremandr.api_services.ApiInterface;
import com.example.phoremandr.helper.SharedPrefHelper;
import com.example.phoremandr.receiver.CallReceiver;

public abstract class BaseActivity extends AppCompatActivity {
    public  static ViewBinding viewBinding;
    public  static ApiInterface apiInterface;
    public static SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding =  getViewModel();
        apiInterface = APIClient.getClient();
        setStatusBarColor(R.color.green_500);
        SharedPrefHelper.getInstance(viewBinding.getRoot().getContext());
        sharedPrefHelper = new SharedPrefHelper();
    }

    public abstract ViewBinding getViewModel();

    public abstract void setStatusBarColor (int color);

    @Override
    protected void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, CallReceiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }



}
