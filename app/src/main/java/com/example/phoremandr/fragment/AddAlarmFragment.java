package com.example.phoremandr.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.adapter.AddAlarmAdapter;
import com.example.phoremandr.api_model.add_alarm.AddAlarmRequestModel;
import com.example.phoremandr.api_request_model.AddAlarmModel;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentAddAlarmBinding;
import com.example.phoremandr.utils.AppValidator;
import com.example.phoremandr.utils.SharedPreferencesKeys;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAlarmFragment extends BaseFragment {
    FragmentAddAlarmBinding addAlarmBinding;
    List<AddAlarmModel> addAlarmModelList;
    AddAlarmAdapter addAlarmAdapter;
    String sound = "", channel = "";

    int position = -1;


    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {
        addAlarmBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_alarm, container, false);
        initView();

        return addAlarmBinding;
    }


    void  initView(){
        addAlarmBinding.addAlarmToolbar.setVisibility(true);
        addAlarmBinding.addAlarmToolbar.setNameData(requireContext().getString(R.string.addAlarmSound));
        addAlarmBinding.addAlarmToolbar.ivBack.setOnClickListener(v -> requireFragmentManager().popBackStack());
        addAlarmModelList = new ArrayList<>();
        addAllAlarm();



        addAlarmAdapter = new AddAlarmAdapter(addAlarmModelList, sharedPrefHelper);
        addAlarmBinding.rvAddAlarm.setHasFixedSize(true);
        addAlarmBinding.rvAddAlarm.setLayoutManager(new LinearLayoutManager(requireContext()));
        addAlarmBinding.rvAddAlarm.setAdapter(addAlarmAdapter);

        addAlarmAdapter.setOnClickListener((position, model) -> {
            AppValidator.logData("getSelectedSound", "" + model.getChannelName());
            sound = model.getSound();
            channel =model.getChannelName();
            this.position  = position;

        });


        addAlarmBinding.btnSubmit.setOnClickListener(v -> onClickButton());


    }


    void addAllAlarm(){

        addAlarmModelList.add(new AddAlarmModel(requireContext().getString(R.string.alarm), "alarm", "alarmChannel"));
        addAlarmModelList.add(new AddAlarmModel(requireContext().getString(R.string.alarm_tone), "alarm_tone", "alarmToneChannel"));
        addAlarmModelList.add(new AddAlarmModel(requireContext().getString(R.string.alert_alarm), "alert_alarm", "alertAlarmChannel"));
        addAlarmModelList.add(new AddAlarmModel(requireContext().getString(R.string.emergency_alarm), "emergency_alarm", "emergencyAlarmChannel"));
    }

    void onClickButton(){
        if(sound.isEmpty()){
            if(sharedPrefHelper.getIntValue(SharedPreferencesKeys.alarm) > -1){
                channel = addAlarmModelList.get(sharedPrefHelper.getIntValue(SharedPreferencesKeys.alarm)).getChannelName();
                sound = addAlarmModelList.get(sharedPrefHelper.getIntValue(SharedPreferencesKeys.alarm)).getSound();
            }
        }

        if(sound.isEmpty() ){
            AppValidator.showToast(requireContext(), requireContext().getString(R.string.select_alarm));
        }else {
           callSettingApi();
        }
    }



    void callSettingApi(){
        addAlarmBinding.addAlarmProgress.setVisibility(View.VISIBLE);

        Call<AddAlarmRequestModel> call3 = apiInterface.callAddAlarmApi(
                sharedPrefHelper.getValue(SharedPreferencesKeys.userId),channel, sound
        );

        call3.enqueue(new Callback<AddAlarmRequestModel>() {
            @Override
            public void onResponse(@NotNull Call<AddAlarmRequestModel> call, @NotNull Response<AddAlarmRequestModel> response) {

                addAlarmBinding.addAlarmProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(requireActivity(), response.body().getMessage());
                if(response.body().getCode().equals("200")){
                    sharedPrefHelper.setIntValue(SharedPreferencesKeys.alarm, position);
                    requireFragmentManager().popBackStack();
                }



            }
            @Override
            public void onFailure(@NotNull  Call<AddAlarmRequestModel> call,@NotNull Throwable t) {
                addAlarmBinding.addAlarmProgress.setVisibility(View.GONE);
                AppValidator.logData("addAlarmError",""+t.getMessage()+ " "+ call.request());
            }
        });

    }

}
