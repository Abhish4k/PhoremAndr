package com.example.phoremandr.fragment;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;
import com.example.phoremandr.R;
import com.example.phoremandr.api_model.get_memo_by_id.GetMemoByIdDataResponse;
import com.example.phoremandr.api_model.get_memo_by_id.GetMemoByIdResponse;
import com.example.phoremandr.api_request_model.CreateMemoRequestModel;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentCreateMemoBinding;
import com.example.phoremandr.utils.AppValidator;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMemoFragment extends BaseFragment {

    private MediaRecorder recorder;
    private boolean isRecording = false;

    public static final int PERMISSION_CODE = 1;

    FragmentCreateMemoBinding memoBinding;
    boolean isVisible, isEdit;

    String name, memoId;
    String selectedDate, time;
    String audioUrl = "";
    CreateMemoFragment(boolean isVisible, boolean isEdit, String name, String memoId){
        this.isVisible = isVisible;
        this.isEdit = isEdit;
        this.name = name;
        this.memoId = memoId;
    }
    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {

        memoBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_create_memo, container, false);
        initView();
        return memoBinding;

    }




    @SuppressLint("UseCompatLoadingForDrawables")
    void initView(){

        memoBinding.memoToolbar.setNameData(name);

        memoBinding.memoToolbar.setVisibility(isVisible);

        memoBinding.memoToolbar.ivBack.setOnClickListener(v -> {
           requireFragmentManager().popBackStack();

        });

        if(isEdit){
            callMemoView();
        }

        memoBinding.tvDateTime.setOnClickListener(v -> showDatePickerDialog());

        memoBinding.ivMic.setOnClickListener(v -> {
            if (checkRecordingPermission()) {
                if (!isRecording) {
                    isRecording = true;
                    memoBinding.chronometer.setBase(SystemClock.elapsedRealtime());
                    memoBinding.chronometer.start();
                    startRecording();
                    memoBinding.ivMic.setImageDrawable(requireContext().getDrawable(R.drawable.stop));

                    Handler handler = new Handler();
                    handler.postDelayed(this::stopRecording, 30000);

                } else {
                    stopRecording();
                }
            } else {
                requestRecordingPermission();
            }
        });


        memoBinding.btnSubmit.setOnClickListener(v -> onClickSubmitButton());

    }


    @SuppressLint("SimpleDateFormat")
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dd = new DatePickerDialog(requireActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {

                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        Date date = formatter.parse(dateInString);



                        formatter = new SimpleDateFormat("yyyy-MM-dd");

                        assert date != null;
                        selectedDate = formatter.format(date);

                        showTimePickerDialog();


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }, mYear, mMonth, mDay);
        dd.show();

    }

    @SuppressLint("SetTextI18n")
    private  void showTimePickerDialog() {


        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(requireContext(), (timePicker, selectedHour, selectedMinute) -> {
            time = selectedHour + ":" + selectedMinute ;
            memoBinding.tvDateTime.setText(selectedDate + " " + time);
        }
             , hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");


        mTimePicker.show();


    }



    @SuppressLint("UseCompatLoadingForDrawables")
    void  stopRecording(){
        isRecording = false;
        memoBinding.chronometer.stop();
        memoBinding.chronometer.setBase(SystemClock.elapsedRealtime());
        saveRecording();
        Toast.makeText(getContext(), "Recording Saved", Toast.LENGTH_SHORT).show();
        memoBinding.ivMic.setImageDrawable(requireContext().getDrawable(R.drawable.mic));
    }


    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private void startRecording() {
        executorService.execute(() -> {
            memoBinding.chronometer.setBase(SystemClock.elapsedRealtime());
            memoBinding.chronometer.start();
        });
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // Change this line
        recorder.setOutputFile(getRecordingFilePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recorder.start();



    }



    private void saveRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;


        String directory = getRecordingFilePath();

        AppValidator.logData("file","" + directory);
        executorService.execute(() -> {
            memoBinding.chronometer.stop();
            memoBinding.chronometer.setBase(SystemClock.elapsedRealtime());
        });
    }



    private void  requestRecordingPermission()
    {
        ActivityCompat.requestPermissions(requireActivity() , new String[]{Manifest.permission.RECORD_AUDIO} ,PERMISSION_CODE );
    }

    public boolean checkRecordingPermission() {
        if (ContextCompat.checkSelfPermission( requireContext() ,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_DENIED){
            requestRecordingPermission();
            return false;
        }
        return true;
    }




    private  String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(requireContext());
        File music = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_AUDIOBOOKS);
        }else {
            music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        }

        AppValidator.logData("musicDirectory","" + music);
        File file = new File(music, "audio"+".mp3");
        return  file.getPath();
    }


    void callMemoView(){
        memoBinding.createMemoProgress.setVisibility(View.VISIBLE);
        Call<GetMemoByIdResponse> call3 = apiInterface.callGetMemoById(memoId);

        call3.enqueue(new Callback<GetMemoByIdResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetMemoByIdResponse> call, @NotNull Response<GetMemoByIdResponse> response) {
                memoBinding.createMemoProgress.setVisibility(View.GONE);
                assert response.body() != null;

                if (response.body().getCode().equals("200")){
                    GetMemoByIdDataResponse getMemoByIdDataResponse = response.body().getData();

                    memoBinding.etName.setText(getMemoByIdDataResponse.getName());
                    memoBinding.etMemoName.setText(getMemoByIdDataResponse.getMemo());
                    memoBinding.etPhone.setText(getMemoByIdDataResponse.getPhoneNumber());
                    memoBinding.tvDateTime.setText(getMemoByIdDataResponse.getReminder());

                    audioUrl = getMemoByIdDataResponse.getVoiceMemo();

                }

            }
            @Override
            public void onFailure(@NotNull  Call<GetMemoByIdResponse> call, @NotNull Throwable t) {
                memoBinding.createMemoProgress.setVisibility(View.GONE);
                AppValidator.logData("getMemoById",""+t.getMessage());
            }
        });
    }

    void onClickSubmitButton(){
        CreateMemoRequestModel createMemoRequestModel = new CreateMemoRequestModel(
                memoBinding.etName.getText().toString().trim(),
                memoBinding.etMemoName.getText().toString().trim(),
                memoBinding.tvDateTime.getText().toString().trim(),
                memoBinding.etPhone.getText().toString().trim(),
                getRecordingFilePath()
        );

        if(AppValidator.validateCreateMemo(requireContext(), createMemoRequestModel)){

        }
    }

}