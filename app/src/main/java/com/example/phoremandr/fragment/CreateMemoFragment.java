package com.example.phoremandr.fragment;
import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
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
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentCreateMemoBinding;
import com.example.phoremandr.utils.AppValidator;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
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




    void initView(){

        memoBinding.memoToolbar.setNameData(name);

        memoBinding.memoToolbar.setVisibility(isVisible);

        memoBinding.memoToolbar.ivBack.setOnClickListener(v -> {
           requireFragmentManager().popBackStack();

        });

        if(isEdit){
            callMemoView();
        }

        memoBinding.buttonStart.setOnClickListener(v -> {
            if (checkRecordingPermission()) {
                if (!isRecording) {
                    isRecording = true;
                    memoBinding.chronometer.setBase(SystemClock.elapsedRealtime());
                    memoBinding.chronometer.start();
                    startRecording();
                    memoBinding.buttonStart.setText(requireContext().getString(R.string.stop));
                } else {
                    isRecording = false;
                    memoBinding.chronometer.stop();
                    memoBinding.chronometer.setBase(SystemClock.elapsedRealtime());
                    saveRecording();
                    Toast.makeText(getContext(), "Recording Saved", Toast.LENGTH_SHORT).show();
                    memoBinding.buttonStart.setText(requireContext().getString(R.string.start));
                }
            } else {
                requestRecordingPermission();
            }
        });

    }



    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private void startRecording() {
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


        executorService.execute(() -> {
            memoBinding.chronometer.setBase(SystemClock.elapsedRealtime());
            memoBinding.chronometer.start();
        });
    }



    private void saveRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        // Update the Chronometer UI on the main thread
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

        AppValidator.logData("musicDirectory============>","" + music);
        File file = new File(music, "testfile"+".mp3");
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
}