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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.api_model.RegisterResponse;
import com.example.phoremandr.api_model.get_memo_by_id.GetMemoByIdDataResponse;
import com.example.phoremandr.api_model.get_memo_by_id.GetMemoByIdResponse;
import com.example.phoremandr.api_request_model.CreateMemoRequestModel;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentCreateMemoBinding;
import com.example.phoremandr.utils.AppValidator;
import com.example.phoremandr.utils.SharedPreferencesKeys;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    String audioUrl = "", audioPath = "";
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
      if(isRecording){
          isRecording = false;
          memoBinding.chronometer.stop();
         // memoBinding.chronometer.setBase(SystemClock.elapsedRealtime());
          saveRecording();
          Toast.makeText(getContext(), "Recording Saved", Toast.LENGTH_SHORT).show();
          memoBinding.ivMic.setImageDrawable(requireContext().getDrawable(R.drawable.mic));
      }
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
        if(isRecording){
            recorder.stop();
            recorder.release();
            recorder = null;
        }



        audioPath = getRecordingFilePath();

        AppValidator.logData("file","" + audioPath);
        executorService.execute(() -> {
            memoBinding.chronometer.stop();
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
        if(isRecording){
            stopRecording();
        }

        AppValidator.logData("dateTime","" + memoBinding.tvDateTime.getText().toString());
        CreateMemoRequestModel createMemoRequestModel = new CreateMemoRequestModel(
                memoBinding.etName.getText().toString().trim(),
                memoBinding.etMemoName.getText().toString().trim(),
                memoBinding.tvDateTime.getText().toString(),
                memoBinding.etPhone.getText().toString().trim(),
                audioPath
        );

        if(AppValidator.validateCreateMemo(requireContext(), createMemoRequestModel)){
           if(!isEdit){
               if(createMemoRequestModel.getVoiceMemo().isEmpty()){
                   callApiWithoutVoiceMemo(createMemoRequestModel);
               }else {
                   callApiWithVoiceMemo(createMemoRequestModel);
               }
           }else if(isEdit){
               if(!audioUrl.isEmpty()){
                   createMemoRequestModel.setVoiceMemo(audioUrl);
               }

               AppValidator.logData("audioUrl", "" + createMemoRequestModel.getVoiceMemo());
               if(createMemoRequestModel.getVoiceMemo().isEmpty()){
                   callApiEditWithoutVoiceMemo(createMemoRequestModel);
               }else {
                   callEditApiWithVoiceMemo(createMemoRequestModel);
               }
           }
        }
    }


    void callApiWithVoiceMemo(CreateMemoRequestModel createMemoRequestModel){

        memoBinding.createMemoProgress.setVisibility(View.VISIBLE);

        File file = new File(createMemoRequestModel.getVoiceMemo());
        AppValidator.logData("voiceMemoFile","" +file.getAbsoluteFile());

        RequestBody name = RequestBody.create(createMemoRequestModel.getName(), MediaType.parse("text/plain"));
        RequestBody userId = RequestBody.create(sharedPrefHelper.getValue(SharedPreferencesKeys.userId),MediaType.parse("text/plain"));
        RequestBody phoneNumber = RequestBody.create(createMemoRequestModel.getPhoneNumber(),MediaType.parse("text/plain"));
        RequestBody memo = RequestBody.create(createMemoRequestModel.getMemoName(),MediaType.parse("text/plain"));
        RequestBody reminder = RequestBody.create(createMemoRequestModel.getReminder(),MediaType.parse("text/plain"));
        RequestBody requestFile = RequestBody.create(file,MediaType.parse("audio/mp3"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("voice_memo", file.getName(), requestFile);

        AppValidator.logData("requestFile","" + body);

        Call<RegisterResponse> call3 = apiInterface.callCreateMemoWithVoiceApi(
                name, userId, phoneNumber, memo,reminder, body
        );

        call3.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NotNull Call<RegisterResponse> call, @NotNull Response<RegisterResponse> response) {

                memoBinding.createMemoProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(requireActivity(), response.body().getMessage());
                if(response.body().getCode().equals("200")){
                    getFragmentManager().popBackStack();
                }



            }
            @Override
            public void onFailure(@NotNull  Call<RegisterResponse> call,@NotNull Throwable t) {
                memoBinding.createMemoProgress.setVisibility(View.GONE);
                AppValidator.logData("createMemoError",""+t.getMessage()+ " "+ call.request());
            }
        });

    }


    void callApiWithoutVoiceMemo(CreateMemoRequestModel createMemoRequestModel){
        memoBinding.createMemoProgress.setVisibility(View.VISIBLE);

        Call<RegisterResponse> call3 = apiInterface.callCreateMemoApi(
                createMemoRequestModel.getName(),
                sharedPrefHelper.getValue(SharedPreferencesKeys.userId),
                createMemoRequestModel.getPhoneNumber(), createMemoRequestModel.getMemoName(),
                createMemoRequestModel.getReminder()
        );

        call3.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NotNull Call<RegisterResponse> call, @NotNull Response<RegisterResponse> response) {

                memoBinding.createMemoProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(requireActivity(), response.body().getMessage());
                if(response.body().getCode().equals("200")){
                    getFragmentManager().popBackStack();
                }



            }
            @Override
            public void onFailure(@NotNull  Call<RegisterResponse> call,@NotNull Throwable t) {
                memoBinding.createMemoProgress.setVisibility(View.GONE);
                AppValidator.logData("createMemoError",""+t.getMessage());
            }
        });

    }


    void callEditApiWithVoiceMemo(CreateMemoRequestModel createMemoRequestModel){

        memoBinding.createMemoProgress.setVisibility(View.VISIBLE);

        File file = new File(createMemoRequestModel.getVoiceMemo());

        RequestBody memoRequestId = RequestBody.create(memoId, MediaType.parse("text/plain"));
        RequestBody name = RequestBody.create(createMemoRequestModel.getName(), MediaType.parse("text/plain"));
        RequestBody userId = RequestBody.create(sharedPrefHelper.getValue(SharedPreferencesKeys.userId),MediaType.parse("text/plain"));
        RequestBody phoneNumber = RequestBody.create(createMemoRequestModel.getPhoneNumber(),MediaType.parse("text/plain"));
        RequestBody memo = RequestBody.create(createMemoRequestModel.getMemoName(),MediaType.parse("text/plain"));
        RequestBody reminder = RequestBody.create(createMemoRequestModel.getReminder(),MediaType.parse("text/plain"));
        RequestBody requestFile = RequestBody.create(file,MediaType.parse("audio/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("voice_memo", file.getName(), requestFile);

        Log.e("","" + body);

        Call<GetMemoByIdResponse> call3 = apiInterface.callEditMemoWithVoiceApi(
                memoRequestId,
                name, userId, phoneNumber, memo,reminder, body
        );

        call3.enqueue(new Callback<GetMemoByIdResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetMemoByIdResponse> call, @NotNull Response<GetMemoByIdResponse> response) {

                memoBinding.createMemoProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(requireActivity(), response.body().getStatus());
                if(response.body().getCode().equals("200")){
                    getFragmentManager().popBackStack();
                }



            }
            @Override
            public void onFailure(@NotNull  Call<GetMemoByIdResponse> call,@NotNull Throwable t) {
                memoBinding.createMemoProgress.setVisibility(View.GONE);
                AppValidator.logData("editVoiceMemoError",""+t.getMessage());
            }
        });




    }


    void callApiEditWithoutVoiceMemo(CreateMemoRequestModel createMemoRequestModel){
        memoBinding.createMemoProgress.setVisibility(View.VISIBLE);

        Call<GetMemoByIdResponse> call3 = apiInterface.callEditMemoApi(
                memoId,
                createMemoRequestModel.getName(),
                sharedPrefHelper.getValue(SharedPreferencesKeys.userId),
                createMemoRequestModel.getPhoneNumber(),
                createMemoRequestModel.getMemoName(),
                createMemoRequestModel.getReminder()
        );

        call3.enqueue(new Callback<GetMemoByIdResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetMemoByIdResponse> call, @NotNull Response<GetMemoByIdResponse> response) {

                memoBinding.createMemoProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(requireActivity(), response.body().getStatus());
                if(response.body().getCode().equals("200")){
                    getFragmentManager().popBackStack();
                }



            }
            @Override
            public void onFailure(@NotNull  Call<GetMemoByIdResponse> call,@NotNull Throwable t) {
                memoBinding.createMemoProgress.setVisibility(View.GONE);
                AppValidator.logData("createMemoError",""+t.getMessage());
            }
        });

    }


}