package com.phorem.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.phorem.R;
import com.phorem.adapter.HomeAdapter;
import com.phorem.api_model.RegisterResponse;
import com.phorem.api_model.get_all_memo.GetAllMemoDataResponse;
import com.phorem.api_model.get_all_memo.GetAllMemoResponse;
import com.phorem.api_services.APIClient;
import com.phorem.base.BaseFragment;
import com.phorem.databinding.FragmentHomeBinding;
import com.phorem.helper.SharedPrefHelper;
import com.phorem.helper.SwipeToDeleteCallback;
import com.phorem.utils.AppValidator;
import com.phorem.utils.SharedPreferencesKeys;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    List<GetAllMemoDataResponse> getAllMemoDataResponseList;
    HomeAdapter homeAdapter;

    boolean isShow;
    public HomeFragment(boolean isShow){
        this.isShow = isShow;
    }
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

    void  initValues() {
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

        if(isShow){
            fragmentHomeBinding.fbCreateMemo.setVisibility(View.VISIBLE);
            fragmentHomeBinding.homeToolbar.ivBack.setVisibility(View.INVISIBLE);


        }else {
            fragmentHomeBinding.fbCreateMemo.setVisibility(View.GONE);
//            fragmentHomeBinding.homeToolbar.ivBack.setVisibility(View.INVISIBLE);
            fragmentHomeBinding.homeToolbar.ivBack.setOnClickListener(v -> requireFragmentManager().popBackStack());

        }

        fragmentHomeBinding.fbCreateMemo.setOnClickListener(v ->
                loadFragment(new CreateMemoFragment(true, false, requireContext().getString(R.string.create_memo),""), requireContext().getString(R.string.home) ));
        homeAdapter.setOnClickListener((position, model) -> {
            AppValidator.logData("getItemId", "" + model.getId());

            loadFragment(new ViewMemoFragment(model.getId().toString()),getString(R.string.home));
        });

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(requireContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                GetAllMemoDataResponse deletedCourse = getAllMemoDataResponseList.get(viewHolder.getAdapterPosition());
                callDeleteMemoId(deletedCourse.getId().toString(), viewHolder);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(fragmentHomeBinding.memoListRV);

        if (!sharedPrefHelper.getValue(SharedPreferencesKeys.userId).isEmpty() || sharedPrefHelper.getValue(SharedPreferencesKeys.userId) != null) {
            callGetAllMemoApi();
        }

    }


    void callGetAllMemoApi(){
        getAllMemoDataResponseList.clear();
        fragmentHomeBinding.homeProgress.setVisibility(View.VISIBLE);
        Call<GetAllMemoResponse> call3 = apiInterface.callGetAllMemoApi(sharedPrefHelper.getValue(SharedPreferencesKeys.userId));

        call3.enqueue(new Callback<GetAllMemoResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<GetAllMemoResponse> call, @NotNull Response<GetAllMemoResponse> response) {

                fragmentHomeBinding.homeProgress.setVisibility(View.GONE);

                if (response.body() != null){
                    AppValidator.showToast(requireActivity(), response.body().getStatus());

                    if(!response.body().getData().isEmpty()){
                        getAllMemoDataResponseList.addAll(response.body().getData());
                        homeAdapter.notifyDataSetChanged();
                    }

                    AppValidator.logData("getAllMemoList", "" + getAllMemoDataResponseList.size());
                }

            }
            @Override
            public void onFailure(@NotNull  Call<GetAllMemoResponse> call,@NotNull Throwable t) {
                fragmentHomeBinding.homeProgress.setVisibility(View.GONE);
                AppValidator.logData("getAllMemo",""+t.getMessage());
            }
        });

    }



    void callDeleteMemoId(String memoId, RecyclerView.ViewHolder viewHolder){

        fragmentHomeBinding.homeProgress.setVisibility(View.VISIBLE);
        Call<RegisterResponse> call3 = apiInterface.callDeleteMemoApi(memoId);

        call3.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NotNull Call<RegisterResponse> call, @NotNull Response<RegisterResponse> response) {
                fragmentHomeBinding.homeProgress.setVisibility(View.GONE);
                assert response.body() != null;
                AppValidator.showToast(requireActivity(), response.body().getMessage());
                if (response.body().getCode().equals("200")){
                    getAllMemoDataResponseList.remove(viewHolder.getAdapterPosition());
                    homeAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }

            }
            @Override
            public void onFailure(@NotNull  Call<RegisterResponse> call, @NotNull Throwable t) {
                fragmentHomeBinding.homeProgress.setVisibility(View.GONE);
                AppValidator.logData("deleteError",""+t.getMessage());
            }
        });
    }




}
