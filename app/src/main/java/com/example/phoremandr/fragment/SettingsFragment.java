package com.example.phoremandr.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

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
        initView();
        return settingsBinding;
    }



    void initView(){
        settingsBinding.tvCreateMemo.setOnClickListener(v -> onClickCreateMemo());
        settingsBinding.tvEditProfile.setOnClickListener(v -> onClickEditProfile());
        settingsBinding.tvContactList.setOnClickListener(v -> onClickContactList());
    }


    void  onClickCreateMemo(){
        loadFragment(new CreateMemoFragment(true, getString(R.string.create_memo)), requireContext().getString(R.string.create_memo));
    }


    void  onClickEditProfile(){
        loadFragment(new EditProfileFragment(), getString(R.string.edit_profile));
    }
    public void onClickContactList(){
        loadFragment(new ContactFragment(true, requireContext().getString(R.string.contact_list)), getString(R.string.contacts));
    }


    public  void  loadFragment(Fragment fragment, String name){
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLay,fragment).addToBackStack(name).commit();
    }





}
