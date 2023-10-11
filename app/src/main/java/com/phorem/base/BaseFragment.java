package com.phorem.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.phorem.R;
import com.phorem.api_services.APIClient;
import com.phorem.api_services.ApiInterface;
import com.phorem.helper.SharedPrefHelper;

public abstract class BaseFragment<ViewDataBinding> extends Fragment {
    public  static ViewBinding viewBinding;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    public  static ApiInterface apiInterface;


    public static SharedPrefHelper sharedPrefHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding =  getViewModel(inflater, container);
        SharedPrefHelper.getInstance(requireContext());
        sharedPrefHelper = new SharedPrefHelper();
        apiInterface = APIClient.getClient();
        return  viewBinding.getRoot();
    }

    public abstract ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container);

    public  void  loadFragment(Fragment fragment, String name){
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLay,fragment).addToBackStack(name).commit();
    }



}