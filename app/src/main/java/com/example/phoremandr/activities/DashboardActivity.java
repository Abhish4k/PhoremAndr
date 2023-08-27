package com.example.phoremandr.activities;

import android.view.MenuItem;

import androidx.annotation.NonNull;
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

public class DashboardActivity extends BaseActivity   implements BottomNavigationView
        .OnNavigationItemSelectedListener{
    ActivityDashboardBinding dashboardBinding;

    @Override
    public ViewBinding getViewModel() {
        dashboardBinding = DataBindingUtil.setContentView(DashboardActivity.this, R.layout.activity_dashboard);
         loadFragment(new HomeFragment());
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
}
