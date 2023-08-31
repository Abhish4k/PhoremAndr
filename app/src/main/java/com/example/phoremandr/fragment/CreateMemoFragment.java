package com.example.phoremandr.fragment;
import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;
import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentCreateMemoBinding;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateMemoFragment extends BaseFragment {

    private MediaRecorder recorder;
    private boolean isRecording = false;
    //sdf
    private final String recordPermission = Manifest.permission.RECORD_AUDIO;
    // constant for storing audio permission
    public static final int PERMISSION_CODE = 1;
    private Chronometer myChronometer;
    FragmentCreateMemoBinding memoBinding;

    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {

        memoBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_create_memo, container, false);
        return memoBinding;

    }

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_memo, container, false);

        myChronometer = view.findViewById(R.id.chronometer);
        Button buttonStart = view.findViewById(R.id.buttonStart);




        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRecordingPermission()) {
                    if (!isRecording) {
                        isRecording = true;
                        myChronometer.setBase(SystemClock.elapsedRealtime());
                        myChronometer.start();
                        startRecording();
                        buttonStart.setText("Stop");
                    } else {
                        isRecording = false;
                        myChronometer.stop();
                        myChronometer.setBase(SystemClock.elapsedRealtime());
                        saveRecording();
                        Toast.makeText(getContext(), "Recording Saved", Toast.LENGTH_SHORT).show();
                        buttonStart.setText("Start");
                    }
                } else {
                    requestRecordingPermission();
                }
            }
        });


        return view;

    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // Change this line
        recorder.setOutputFile(getRecordingFilePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recorder.start();

        // Update the Chronometer UI on the main thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myChronometer.setBase(SystemClock.elapsedRealtime());
                myChronometer.start();
            }
        });
    }



    private void saveRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        // Update the Chronometer UI on the main thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myChronometer.stop();
                myChronometer.setBase(SystemClock.elapsedRealtime());
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode== PERMISSION_CODE){
            if (grantResults.length>0){
                boolean permissionToRecord=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if (permissionToRecord){
                    Toast.makeText(getContext() , "Permission Granted" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext() , "Permission Denied" , Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

    private  String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(requireContext());
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, "testfile"+".mp3");
        return  file.getPath();
    }
}