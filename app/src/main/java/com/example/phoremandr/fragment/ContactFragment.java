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
import com.example.phoremandr.databinding.FragmentContactsBinding;

public class ContactFragment extends BaseFragment {
    FragmentContactsBinding contactsBinding;

    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {
        contactsBinding  = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_contacts, container, false);
        return contactsBinding;
    }
}
