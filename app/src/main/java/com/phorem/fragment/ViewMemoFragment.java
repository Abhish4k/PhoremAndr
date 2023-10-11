package com.phorem.fragment;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.phorem.R;
import com.phorem.api_model.get_memo_by_id.GetMemoByIdDataResponse;
import com.phorem.api_model.get_memo_by_id.GetMemoByIdResponse;
import com.phorem.base.BaseFragment;
import com.phorem.databinding.FragmentViewMemoBinding;
import com.phorem.utils.AppValidator;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMemoFragment extends BaseFragment {
    FragmentViewMemoBinding viewMemoBinding;
    String memoId;
    String audioUrl;

    MediaPlayer mediaPlayer;

    ViewMemoFragment(String memoId){
        this.memoId = memoId;
    }
    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {
        viewMemoBinding   = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_view_memo, container, false);
        initView();
        return viewMemoBinding;
    }

    void  initView(){
        viewMemoBinding.viewMemoToolbar.setVisibility(true);
        viewMemoBinding.viewMemoToolbar.setNameData(requireContext().getString(R.string.view_memo));
        viewMemoBinding.viewMemoToolbar.ivBack.setOnClickListener(v ->{
            if (mediaPlayer != null && mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
            }
            requireFragmentManager().popBackStack();});

        viewMemoBinding.btnPlay.setOnClickListener(v -> {
            if(!audioUrl.isEmpty()){

                playAudio(audioUrl);
            }
        });


        viewMemoBinding.btnEditMemo.setOnClickListener(v -> loadFragment(new CreateMemoFragment(true, true, requireContext().getString(R.string.edit_memo), memoId),requireContext().getString(R.string.view_memo)));


        viewMemoBinding.btnPause.setOnClickListener(v -> {

            if (mediaPlayer != null &&mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        });

        viewMemoBinding.btnResume.setOnClickListener(v -> {

            if (mediaPlayer != null &&!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        });

        viewMemoBinding.btnStop.setOnClickListener(v -> {

            if (mediaPlayer != null && mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        });

        callMemoView();
    }


    void callMemoView(){
        viewMemoBinding.viewMemoProgress.setVisibility(View.VISIBLE);
        Call<GetMemoByIdResponse> call3 = apiInterface.callGetMemoById(memoId);

        call3.enqueue(new Callback<GetMemoByIdResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetMemoByIdResponse> call, @NotNull Response<GetMemoByIdResponse> response) {
                viewMemoBinding.viewMemoProgress.setVisibility(View.GONE);
                if(response.body() != null){
                    if (response.body().getCode().equals("200")){
                        GetMemoByIdDataResponse getMemoByIdDataResponse = response.body().getData();
                        AppValidator.logData("getReminder","" + getMemoByIdDataResponse.getReminder());
                        viewMemoBinding.tvName.setGetName(getMemoByIdDataResponse.getName());
                        viewMemoBinding.tvMemoName.setGetName(getMemoByIdDataResponse.getMemo());
                        viewMemoBinding.tvDate.setGetName(getMemoByIdDataResponse.getReminder());
                        viewMemoBinding.tvPhone.setGetName(getMemoByIdDataResponse.getPhoneNumber());

                        audioUrl = getMemoByIdDataResponse.getVoiceMemo();

                    }
                }


            }
            @Override
            public void onFailure(@NotNull  Call<GetMemoByIdResponse> call, @NotNull Throwable t) {
                viewMemoBinding.viewMemoProgress.setVisibility(View.GONE);
                AppValidator.logData("getMemoById",""+t.getMessage());
            }
        });
    }


    private void playAudio(String audioUrl) {



        // initializing media player
        mediaPlayer = new MediaPlayer();

        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
