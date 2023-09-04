package com.example.phoremandr.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentSettingsBinding;
import com.example.phoremandr.utils.AppValidator;

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
        settingsBinding.tvMemoList.setOnClickListener(v -> onClickMemoList());
        settingsBinding.tvAddAlarm.setOnClickListener(v -> onClickAddAlarm());

        settingsBinding.tvLogout.setOnClickListener(v -> onClickLogout());

    }


    void  onClickCreateMemo(){
        loadFragment(new CreateMemoFragment(true, false, getString(R.string.create_memo), ""), requireContext().getString(R.string.create_memo));
    }


    void  onClickEditProfile(){
        loadFragment(new EditProfileFragment(), getString(R.string.edit_profile));
    }
    public void onClickContactList(){
        loadFragment(new ContactFragment(true, requireContext().getString(R.string.contact_list)), getString(R.string.contacts));
    }



    public void onClickMemoList(){
        loadFragment(new HomeFragment(false), getString(R.string.memo_list));
    }


    public void onClickLogout(){
        AppValidator.showLogoutPopup(requireContext(), sharedPrefHelper);
    }


    public void onClickAddAlarm(){
        loadFragment(new AddAlarmFragment(), requireContext().getString(R.string.select_alarm));

    }

}
