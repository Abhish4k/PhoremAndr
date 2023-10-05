package com.example.phoremandr.activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.api_model.RegisterResponse;
import com.example.phoremandr.api_request_model.CreateMemoRequestModel;
import com.example.phoremandr.base.BaseActivity;
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

public class CreateMemoActivity extends BaseActivity {
    FragmentCreateMemoBinding createMemoActivityBinding;

    private MediaRecorder recorder;

    private boolean isRecording = false;


    String selectedDate = "", time = "";

    String audioUrl = "", audioPath = "";

    public static final int PERMISSION_CODE = 1;

    public CreateMemoActivity() {
    }


    @Override
    public ViewBinding getViewModel() {
        createMemoActivityBinding = DataBindingUtil.setContentView(CreateMemoActivity.this , R.layout.fragment_create_memo);
        initView();
        return createMemoActivityBinding;
    }

    void initView(){
        createMemoActivityBinding.memoToolbar.setNameData(getString(R.string.create_memo));
        createMemoActivityBinding.memoToolbar.setVisibility(true);
        createMemoActivityBinding.memoToolbar.ivBack.setOnClickListener(v -> {
            Intent intent = new Intent(CreateMemoActivity.this , DashboardActivity.class);
            startActivity(intent);

        });






        createMemoActivityBinding.tvDateTime.setOnClickListener(v -> showDatePickerDialog());

        createMemoActivityBinding.ivMic.setOnClickListener(v -> {
            if (checkRecordingPermission()) {
                if (!isRecording) {
                    isRecording = true;
                    createMemoActivityBinding.chronometer.setBase(SystemClock.elapsedRealtime());
                    createMemoActivityBinding.chronometer.start();
                    startRecording();
                    createMemoActivityBinding.ivMic.setImageDrawable(getApplicationContext().getDrawable(R.drawable.stop));
                    Handler handler = new Handler();
                    handler.postDelayed(this::stopRecording, 30000);

                } else {
                    stopRecording();
                }
            } else {
                requestRecordingPermission();
            }
        });


        createMemoActivityBinding.btnSubmit.setOnClickListener(v -> onClickSubmitButton());

    }




    @SuppressLint("SimpleDateFormat")
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dd = new DatePickerDialog(CreateMemoActivity.this,
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
    private void showTimePickerDialog() {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateMemoActivity.this, (timePicker, selectedHour, selectedMinute) -> {
            time = selectedHour + ":" + selectedMinute;
            createMemoActivityBinding.tvDateTime.setText(selectedDate + " " + time);
        }
                , hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");

        mTimePicker.show();

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void stopRecording() {
        if (isRecording) {
            saveRecording();
            isRecording = false;
            createMemoActivityBinding.chronometer.stop();
            // memoBinding.chronometer.setBase(SystemClock.elapsedRealtime());

         /* recorder.stop();
          recorder.release();*/
            AppValidator.showToast(CreateMemoActivity.this, "Recording Saved");
            createMemoActivityBinding.ivMic.setImageDrawable(getApplicationContext().getDrawable(R.drawable.mic));
        }
    }

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void startRecording() {
        executorService.execute(() -> {
            createMemoActivityBinding.chronometer.setBase(SystemClock.elapsedRealtime());
            createMemoActivityBinding.chronometer.start();
        });
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // Change this line
        recorder.setOutputFile(getRecordingFilePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recorder.start();

    }

    private void saveRecording() {
        if (isRecording) {
            recorder.stop();
            recorder.release();
            recorder = null;

        }

        audioPath = getRecordingFilePath();

        AppValidator.logData("file", "" + audioPath);
        executorService.execute(() -> {
            createMemoActivityBinding.chronometer.stop();
        });
    }

    private void requestRecordingPermission() {
        ActivityCompat.requestPermissions(CreateMemoActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_CODE);
    }

    public boolean checkRecordingPermission() {
        if (ContextCompat.checkSelfPermission(CreateMemoActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            requestRecordingPermission();
            return false;
        }
        return true;
    }

    private String getRecordingFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(CreateMemoActivity.this);
        File music = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_AUDIOBOOKS);
        } else {
            music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        }

        AppValidator.logData("musicDirectory", "" + music);
        File file = new File(music, "audio" + ".mp3");
        return file.getPath();
    }


    void onClickSubmitButton() {
        if (isRecording) {
            stopRecording();
        }

        AppValidator.logData("dateTime", "" + createMemoActivityBinding.tvDateTime.getText().toString());

        CreateMemoRequestModel createMemoRequestModel = new CreateMemoRequestModel(
                createMemoActivityBinding.etName.getText().toString().trim(),
                createMemoActivityBinding.etMemoName.getText().toString().trim(),
                createMemoActivityBinding.tvDateTime.getText().toString(),
                createMemoActivityBinding.etPhone.getText().toString().trim(),
                audioPath
        );

        if (AppValidator.validateCreateMemo(CreateMemoActivity.this, createMemoRequestModel)) {
            if (createMemoRequestModel.getVoiceMemo().isEmpty()) {
                callApiWithoutVoiceMemo(createMemoRequestModel);
            } else {
                //    callApiWithoutVoiceMemo(createMemoRequestModel);
                callApiWithVoiceMemo(createMemoRequestModel);
            }
        }
    }




    void callApiWithVoiceMemo(CreateMemoRequestModel createMemoRequestModel){

        createMemoActivityBinding.createMemoProgress.setVisibility(View.VISIBLE);

        File file = new File(createMemoRequestModel.getVoiceMemo());
        long fileSize = file.length();
        Log.d(TAG, "callApiWithVoiceMemo: ============================>>>>"+createMemoRequestModel.getName());

        AppValidator.logData("voiceMemoFile",file.getAbsolutePath());
        RequestBody name = RequestBody.create(createMemoRequestModel.getName(), MediaType.parse("text/plain"));
        RequestBody userId = RequestBody.create(sharedPrefHelper.getValue(SharedPreferencesKeys.userId),MediaType.parse("text/plain"));
        RequestBody phoneNumber = RequestBody.create(createMemoRequestModel.getPhoneNumber(),MediaType.parse("text/plain"));
        RequestBody memo = RequestBody.create(createMemoRequestModel.getMemoName(),MediaType.parse("text/plain"));
        RequestBody reminder = RequestBody.create(createMemoRequestModel.getReminder(),MediaType.parse("text/plain"));
        RequestBody requestFile = RequestBody.create(file,MediaType.parse("audio/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("voice_memo", file.getName(),requestFile);

        AppValidator.logData("requestFile","" + body);

        Call<RegisterResponse> call3 = apiInterface.callCreateMemoWithVoiceApi(
                name, userId, phoneNumber, memo,reminder, body
        );

        call3.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NotNull Call<RegisterResponse> call, @NotNull Response<RegisterResponse> response) {

                createMemoActivityBinding.createMemoProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(CreateMemoActivity.this, response.body().getMessage());
                if(response.body().getCode().equals("200")){
                    startActivity(new Intent(CreateMemoActivity.this, DashboardActivity.class));
                }

            }

            @Override
            public void onFailure(@NotNull  Call<RegisterResponse> call,@NotNull Throwable t) {
                createMemoActivityBinding.createMemoProgress.setVisibility(View.GONE);
                AppValidator.logData("createMemoError",""+t.getMessage()+ " "+ call.request());
            }
        });

    }


    void callApiWithoutVoiceMemo(CreateMemoRequestModel createMemoRequestModel){
        createMemoActivityBinding.createMemoProgress.setVisibility(View.VISIBLE);

        Call<RegisterResponse> call3 = apiInterface.callCreateMemoApi(
                createMemoRequestModel.getName(),
                sharedPrefHelper.getValue(SharedPreferencesKeys.userId),
                createMemoRequestModel.getPhoneNumber(), createMemoRequestModel.getMemoName(),
                createMemoRequestModel.getReminder()
        );

        call3.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NotNull Call<RegisterResponse> call, @NotNull Response<RegisterResponse> response) {

                createMemoActivityBinding.createMemoProgress.setVisibility(View.GONE);

                assert response.body() != null;
                AppValidator.showToast(CreateMemoActivity.this, response.body().getMessage());
                if(response.body().getCode().equals("200")){
                   startActivity(new Intent(CreateMemoActivity.this, DashboardActivity.class));
                }



            }
            @Override
            public void onFailure(@NotNull  Call<RegisterResponse> call,@NotNull Throwable t) {
                createMemoActivityBinding.createMemoProgress.setVisibility(View.GONE);
                AppValidator.logData("createMemoError",""+t.getMessage());
            }
        });

    }







    @Override
    public void setStatusBarColor(int color) {

    }
}
