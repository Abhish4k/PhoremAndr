package com.example.phoremandr.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentHomeBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class HomeFragment extends BaseFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    FragmentHomeBinding fragmentHomeBinding;

    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {
        fragmentHomeBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.fragment_home, container, false);

        return  fragmentHomeBinding;

    }



}
