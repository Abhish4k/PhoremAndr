package com.example.phoremandr.adapter;

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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoremandr.R;
import com.example.phoremandr.api_model.get_all_memo.GetAllMemoDataResponse;
import com.example.phoremandr.api_request_model.AddAlarmModel;

import java.io.IOException;
import java.util.List;

public class AddAlarmAdapter  extends RecyclerView.Adapter<AddAlarmAdapter.ViewHolder>{
    private List<AddAlarmModel> listData;

    public static OnClickListener onClickListener;
    public int selectedPosition = -1;
    Context context;
    MediaPlayer mediaPlayer;

    public AddAlarmAdapter(List<AddAlarmModel> listData) {
        this.listData = listData;
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

        holder.llSound.setOnClickListener(v -> {

            selectedPosition = holder.getAdapterPosition();
            holder.rbSound.setChecked(position == selectedPosition);
            notifyDataSetChanged();
            if (onClickListener != null) {
                onClickListener.onClick(position, myListData);
            }

        });

        holder.tvSound.setText(myListData.getSoundName());

        holder.rbSound.setChecked(position == selectedPosition);
        holder.rbSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                selectedPosition = holder.getAdapterPosition();
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



    void stopSound(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    void  playSound(String sound){

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
