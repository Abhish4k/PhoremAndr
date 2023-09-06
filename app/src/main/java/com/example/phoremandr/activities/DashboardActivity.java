package com.example.phoremandr.activities;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.ActivityDashboardBinding;
import com.example.phoremandr.fragment.ContactFragment;
import com.example.phoremandr.fragment.CreateMemoFragment;
import com.example.phoremandr.fragment.HomeFragment;
import com.example.phoremandr.fragment.SettingsFragment;
import com.example.phoremandr.utils.AppValidator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.AccessControlContext;


public class DashboardActivity extends BaseActivity   implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static  final String READ_CALL_LOGS = Manifest.permission.READ_CALL_LOG;
    private static  final String PHONE = Manifest.permission.CALL_PHONE;

    private static final  String NOTIFICATION = Manifest.permission.POST_NOTIFICATIONS;
    private static  final int REQUEST_CODE = 200;
    ActivityDashboardBinding dashboardBinding;
    boolean isDashboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkPermission()){
            Toast.makeText(this , "Permissions Already Granted !", Toast.LENGTH_SHORT).show();
        }else {
            ActivityCompat.requestPermissions(this ,
                    new String[]{PHONE,READ_CALL_LOGS, NOTIFICATION} ,REQUEST_CODE );
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

        Intent intent = getIntent();
         isDashboard = intent.getBooleanExtra("isDashboard", false);

        AppValidator.logData("isDashboard","" + isDashboard);

        if(isDashboard){
            loadFragment(new CreateMemoFragment(true, false, getString(R.string.create_memo), ""), getString(R.string.create_memo));
        }else {

            loadFragment(new HomeFragment(true), getString(R.string.home));
        }
        return dashboardBinding;
    }

    @Override
    public void setStatusBarColor(int color) {

    }



    public  void  loadFragment(Fragment fragment, String name){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLay,fragment).addToBackStack(name).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.bnHome:

                loadFragment(new HomeFragment(true), getString(R.string.home));
                break;
            case R.id.bnContacts:

                loadFragment(new ContactFragment(false, ""), getString(R.string.contacts));
                break;
            case R.id.bnSettings:
                loadFragment(new SettingsFragment(), getString(R.string.settings));
                break;
        }

        return true;
    }

    public  boolean checkPermission(){
       int callLogPermission =  ActivityCompat.checkSelfPermission(this , READ_CALL_LOGS );
       int phonePermission = ActivityCompat.checkSelfPermission(this , PHONE);
       int notification  = ActivityCompat.checkSelfPermission(this, NOTIFICATION);

       return callLogPermission ==PackageManager.PERMISSION_GRANTED && phonePermission == PackageManager.PERMISSION_GRANTED && notification == PackageManager.PERMISSION_GRANTED;
    }

}

