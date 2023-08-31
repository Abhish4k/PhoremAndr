package com.example.phoremandr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.adapter.HomeAdapter;
import com.example.phoremandr.api_model.get_all_memo.GetAllMemoDataResponse;
import com.example.phoremandr.api_model.get_all_memo.GetAllMemoResponse;
import com.example.phoremandr.api_model.get_user_profile.GetUserProfileResponse;
import com.example.phoremandr.api_model.get_user_profile.GetUserProfileResponseData;
import com.example.phoremandr.api_services.APIClient;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentHomeBinding;
import com.example.phoremandr.helper.SharedPrefHelper;
import com.example.phoremandr.utils.AppValidator;
import com.example.phoremandr.utils.SharedPreferencesKeys;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    List<GetAllMemoDataResponse> getAllMemoDataResponseList;
    HomeAdapter homeAdapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    FragmentHomeBinding fragmentHomeBinding;

    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {
        fragmentHomeBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.fragment_home, container, false);

       initValues();
        return  fragmentHomeBinding;

    }

    void  initValues(){
        fragmentHomeBinding.homeToolbar.setNameData(requireContext().getString(R.string.memos));
        fragmentHomeBinding.homeToolbar.setVisibility(true);

        getAllMemoDataResponseList = new ArrayList<>();
        SharedPrefHelper.getInstance(requireContext());
        sharedPrefHelper = new SharedPrefHelper();
        apiInterface = APIClient.getClient();

        homeAdapter = new HomeAdapter(getAllMemoDataResponseList);
        fragmentHomeBinding.memoListRV.setHasFixedSize(true);
        fragmentHomeBinding.memoListRV.setLayoutManager(new LinearLayoutManager(requireContext()));
        fragmentHomeBinding.memoListRV.setAdapter(homeAdapter);

        if(!sharedPrefHelper.getValue(SharedPreferencesKeys.userId).isEmpty() || sharedPrefHelper.getValue(SharedPreferencesKeys.userId) != null){
            callGetAllMemoApi();
        }

    }



    void callGetAllMemoApi(){
        getAllMemoDataResponseList.clear();
        fragmentHomeBinding.homeProgress.setVisibility(View.VISIBLE);
        Call<GetAllMemoResponse> call3 = apiInterface.callGetAllMemoApi(sharedPrefHelper.getValue(SharedPreferencesKeys.userId));

        call3.enqueue(new Callback<GetAllMemoResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetAllMemoResponse> call, @NotNull Response<GetAllMemoResponse> response) {

                fragmentHomeBinding.homeProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(requireActivity(), response.body().getStatus());
                if(!response.body().getData().isEmpty()){
                    getAllMemoDataResponseList.addAll(response.body().getData());
                    homeAdapter.notifyDataSetChanged();
                }

                AppValidator.logData("getAllMemoList", "" + getAllMemoDataResponseList.size());
            }
            @Override
            public void onFailure(@NotNull  Call<GetAllMemoResponse> call,@NotNull Throwable t) {
                fragmentHomeBinding.homeProgress.setVisibility(View.GONE);
                AppValidator.logData("getAllMemo",""+t.getMessage());
            }
        });

    }



}
