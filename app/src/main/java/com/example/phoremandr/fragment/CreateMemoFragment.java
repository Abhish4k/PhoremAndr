package com.example.phoremandr.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentCreateMemoBinding;

public class CreateMemoFragment extends BaseFragment {
    FragmentCreateMemoBinding memoBinding;

    boolean isVisible;
    String name;
    CreateMemoFragment(boolean isVisible, String name){
        this.isVisible = isVisible;
        this.name = name;
    }
    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {

        memoBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_create_memo, container, false);

        memoBinding.memoToolbar.setVisibility(isVisible);
        memoBinding.memoToolbar.setNameData(name);

        memoBinding.memoToolbar.ivBack.setOnClickListener(v -> getFragmentManager().popBackStack());
        return memoBinding;
    }
}
