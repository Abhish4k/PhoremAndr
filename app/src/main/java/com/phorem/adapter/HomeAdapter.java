package com.phorem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.phorem.R;
import com.phorem.api_model.get_all_memo.GetAllMemoDataResponse;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    private List<GetAllMemoDataResponse> listData;

    public static OnClickListener onClickListener;
    Context context;

    public HomeAdapter(List<GetAllMemoDataResponse> listData) {
        this.listData = listData;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.rv_home_content, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GetAllMemoDataResponse myListData = listData.get(position);

        holder.tvNumber.setText(myListData.getPhoneNumber());
        holder.tvMemoName.setText(myListData.getMemo());
        holder.tvName.setText(myListData.getName());
        holder.tvDate.setText(myListData.getReminder());

        holder.cvMemo.setOnClickListener(v ->     {
            if (onClickListener != null) {
                onClickListener.onClick(position, myListData);}
        });


    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvDate, tvMemoName,tvNumber ;
        public CardView cvMemo;
        public ViewHolder(View itemView) {
            super(itemView);
            this.cvMemo = itemView.findViewById(R.id.cvMemo);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvDate = itemView.findViewById(R.id.tvDate);
            this.tvMemoName = itemView.findViewById(R.id.tvMemoName);
            this.tvNumber = itemView.findViewById(R.id.tvNumber);
        }
    }

    public  void  setOnClickListener(OnClickListener onClickListener) {
       this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, GetAllMemoDataResponse model);
    }
}
