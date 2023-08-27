package com.example.phoremandr.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.helper.SharedPrefHelper;

public abstract class BaseFragment<ViewDataBinding> extends Fragment {
    public  static ViewBinding viewBinding;




    public static SharedPrefHelper sharedPrefHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewBinding =  getViewModel(inflater, container);
        SharedPrefHelper.getInstance(requireContext());
        sharedPrefHelper = new SharedPrefHelper();
        return  viewBinding.getRoot();
    }




    public abstract ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container);



}