package com.phorem.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.phorem.R;
import com.phorem.api_request_model.AddAlarmModel;
import com.phorem.helper.SharedPrefHelper;
import com.phorem.utils.AppValidator;
import com.phorem.utils.SharedPreferencesKeys;

import java.io.IOException;
import java.util.List;

public class AddAlarmAdapter  extends RecyclerView.Adapter<AddAlarmAdapter.ViewHolder>{
    private List<AddAlarmModel> listData;

    public static OnClickListener onClickListener;
    public int selectedPosition = -1;
    Context context;
    MediaPlayer mediaPlayer;
    SharedPrefHelper sharedPrefHelper;

    public AddAlarmAdapter(List<AddAlarmModel> listData, SharedPrefHelper sharedPrefHelper) {
        this.listData = listData;
        this.sharedPrefHelper = sharedPrefHelper;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.rv_add_alarm, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder holder, int  position) {
        final AddAlarmModel myListData = listData.get(position);


        if(sharedPrefHelper.getIntValue(SharedPreferencesKeys.alarm) > -1 && selectedPosition == -1 ){
            AppValidator.logData("alarmSelected","" + sharedPrefHelper.getIntValue(SharedPreferencesKeys.alarm));
            selectedPosition = sharedPrefHelper.getIntValue(SharedPreferencesKeys.alarm);
        }

        holder.llSound.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            holder.rbSound.setChecked(position == selectedPosition);
            notifyDataSetChanged();
            stopSound();
            if (onClickListener != null) {
                onClickListener.onClick(position, myListData);
            }

        });

        holder.tvSound.setText(myListData.getSoundName());

        holder.rbSound.setChecked(position == selectedPosition);
        holder.rbSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                selectedPosition = holder.getAdapterPosition();
                holder.rbSound.setChecked(position == selectedPosition);
                stopSound();
                onClickListener.onClick(holder.getAdapterPosition(), myListData);
                notifyDataSetChanged();
            }

        });


        mediaPlayer = new MediaPlayer();
        holder.btnStop.setOnClickListener(v -> stopSound());
        holder.btnPlay.setOnClickListener(v -> playSound(myListData.getSound()));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvSound;
        public Button btnPlay, btnStop;
        public RadioButton rbSound;
        public LinearLayout llSound;

        public ViewHolder(View itemView) {
            super(itemView);
            rbSound = itemView.findViewById(R.id.rbSound);
            tvSound = itemView.findViewById(R.id.tvSound);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            btnStop = itemView.findViewById(R.id.btnStop);
            llSound =itemView.findViewById(R.id.llSound);


        }
    }

    public  void  setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, AddAlarmModel model);
    }



    public void stopSound(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    void  playSound(String sound){
        stopSound();
        mediaPlayer = new MediaPlayer();
        AssetFileDescriptor afd;
        try {
            afd = context.getAssets().openFd(sound);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
