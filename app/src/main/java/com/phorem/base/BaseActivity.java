package com.phorem.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import com.phorem.R;
import com.phorem.api_services.APIClient;
import com.phorem.api_services.ApiInterface;
import com.phorem.databinding.ActivitySignupBinding;
import com.phorem.helper.SharedPrefHelper;
import com.phorem.receiver.CallReceiver;

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
       // createChannel();
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
