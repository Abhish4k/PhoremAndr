package com.example.phoremandr.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.adapter.AddAlarmAdapter;
import com.example.phoremandr.api_request_model.AddAlarmModel;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentAddAlarmBinding;
import com.example.phoremandr.utils.AppValidator;

import java.util.ArrayList;
import java.util.List;

public class AddAlarmFragment extends BaseFragment {
    FragmentAddAlarmBinding addAlarmBinding;
    List<AddAlarmModel> addAlarmModelList;
    AddAlarmAdapter addAlarmAdapter;
    String sound = "";


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



        addAlarmAdapter = new AddAlarmAdapter(addAlarmModelList);
        addAlarmBinding.rvAddAlarm.setHasFixedSize(true);
        addAlarmBinding.rvAddAlarm.setLayoutManager(new LinearLayoutManager(requireContext()));
        addAlarmBinding.rvAddAlarm.setAdapter(addAlarmAdapter);

        addAlarmAdapter.setOnClickListener((position, model) -> {
            AppValidator.logData("getSelectedSound", "" + model.getSoundName());
            sound = model.getSound();

        });


        addAlarmBinding.btnSubmit.setOnClickListener(v -> onClickButton());


    }


    void addAllAlarm(){

        addAlarmModelList.add(new AddAlarmModel(requireContext().getString(R.string.alarm), "alarm.mp3"));
        addAlarmModelList.add(new AddAlarmModel(requireContext().getString(R.string.alarm_tone), "alarm_tone.wav"));
        addAlarmModelList.add(new AddAlarmModel(requireContext().getString(R.string.alert_alarm), "alert_alarm.wav"));
        addAlarmModelList.add(new AddAlarmModel(requireContext().getString(R.string.emergency_alarm), "emergency_alarm.wav"));
    }

    void onClickButton(){
        if(sound.isEmpty()){
            AppValidator.showToast(requireContext(), requireContext().getString(R.string.select_alarm));
        }else {
            requireFragmentManager().popBackStack();
        }
    }


}
