package com.example.phoremandr.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.ActivityDashboardBinding;
import com.example.phoremandr.fragment.ContactFragment;
import com.example.phoremandr.fragment.HomeFragment;
import com.example.phoremandr.fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class DashboardActivity extends BaseActivity   implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static  final String READ_CALL_LOGS = Manifest.permission.READ_CALL_LOG;
    private static  final String PHONE = Manifest.permission.CALL_PHONE;
    private static  final int REQUEST_CODE = 200;
    ActivityDashboardBinding dashboardBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkPermission()){
            Toast.makeText(this , "Permissions Already Granted !", Toast.LENGTH_SHORT).show();
        }else {
            ActivityCompat.requestPermissions(this ,
                    new String[]{PHONE,READ_CALL_LOGS} ,REQUEST_CODE );
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== REQUEST_CODE){
            if (grantResults.length>0){
                int phone = grantResults[0];
                int read_call_logs = grantResults[1];

                boolean checkPhone = phone ==PackageManager.PERMISSION_GRANTED;
                boolean checkCallLog = read_call_logs == PackageManager.PERMISSION_GRANTED;

                if(checkPhone && checkCallLog){
                    Toast.makeText(this , "Permissions Granted !", Toast.LENGTH_SHORT).show();


                }else{
                    Toast.makeText(this , "Permissions Denied !", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public ViewBinding getViewModel() {
        dashboardBinding = DataBindingUtil.setContentView(DashboardActivity.this, R.layout.activity_dashboard);
        dashboardBinding.bottomNavigation
                .setOnNavigationItemSelectedListener(this);
        dashboardBinding.bottomNavigation.setSelectedItemId(R.id.bnHome);
        return dashboardBinding;
    }

    @Override
    public void setStatusBarColor(int color) {

    }



    public  void  loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLay,fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.bnHome:
                loadFragment(new HomeFragment());
                break;
            case R.id.bnContacts:
                loadFragment(new ContactFragment());
                break;
            case R.id.bnSettings:
                loadFragment(new SettingsFragment());
                break;
        }

        return true;
    }

    public  boolean checkPermission(){
       int callLogPermission =  ActivityCompat.checkSelfPermission(this , READ_CALL_LOGS );
       int phonePermission = ActivityCompat.checkSelfPermission(this , PHONE);

       return callLogPermission ==PackageManager.PERMISSION_GRANTED && phonePermission == PackageManager.PERMISSION_GRANTED;
    }

}

