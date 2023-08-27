package com.example.phoremandr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentSettingsBinding;

public class SettingsFragment extends BaseFragment {
   FragmentSettingsBinding settingsBinding;

    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {
        settingsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_settings, container,false);
        return settingsBinding;
    }
}
