package com.example.phoremandr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentHomeBinding;

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

        fragmentHomeBinding.homeToolbar.setNameData(requireContext().getString(R.string.memos));
        fragmentHomeBinding.homeToolbar.setVisibility(true);

        return  fragmentHomeBinding;

    }



}
