package com.example.phoremandr.activities;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseActivity;
import com.example.phoremandr.databinding.ActivityDashboardBinding;

public class DashboardActivity extends BaseActivity implements View.OnClickListener {
    ActivityDashboardBinding dashboardBinding;
    @Override
    public void onClick(View v) {

    }

    @Override
    public ViewBinding getViewModel() {
        dashboardBinding = DataBindingUtil.setContentView(DashboardActivity.this, R.layout.activity_dashboard);
        return dashboardBinding;
    }

    @Override
    public void setStatusBarColor(int color) {

    }
}
