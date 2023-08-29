package com.example.phoremandr.fragment;
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;
import com.example.phoremandr.R;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentCreateMemoBinding;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class CreateMemoFragment extends BaseFragment {

    private MediaRecorder recorder;
    private boolean isRecording = false;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private String fileName;

    // constant for storing audio permission
    public static final int PERMISSION_CODE = 1;
    private Chronometer myChronometer;
    FragmentCreateMemoBinding memoBinding;
    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {

        memoBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_create_memo, container, false);
        return memoBinding;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_create_memo, container, false);

            myChronometer = view.findViewById(R.id.chronometer);
            Button buttonStart = view.findViewById(R.id.buttonStart);

            buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isRecording){
                        myChronometer.setBase(SystemClock.elapsedRealtime());
                        myChronometer.start();
                        checkPermissionss();
                        startRecording();
                        buttonStart.setText("Stop");
                        isRecording =true;
                    }else{
                        myChronometer.stop();
                        myChronometer.setBase(SystemClock.elapsedRealtime());
                        saveRecording();
                        Toast.makeText(getContext() , "Recording Saved" , Toast.LENGTH_SHORT).show();
                        buttonStart.setText("Start");
                        isRecording =false;

                    }

                }
            });

            return view;

    }

    private void startRecording() {
        String filePath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date date = new Date();
        fileName = "Recording_"+ formatter.format(date) + ".3gp";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filePath+ "/" +fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();
    }

    private void saveRecording () {//hitting the save button will generate the audio file
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public boolean checkPermissionss() {
        // this method is used to check permission
        if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {recordPermission}, PERMISSION_CODE );
            return false;
        }
    }



}
