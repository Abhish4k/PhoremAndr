package com.example.phoremandr.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentEditProfileBinding;

public class EditProfileFragment extends BaseFragment {
    FragmentEditProfileBinding editProfileBinding;
    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {
        editProfileBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_edit_profile, container, false);
        return editProfileBinding;
    }

    public void  onClickUpdateProfile(View view){

    }
}
