package com.example.phoremandr.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.phoremandr.R;
import com.example.phoremandr.api_request_model.ContactListModel;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{
    private static List<ContactListModel> listData;
    private List<ContactListModel>filteredList;
    Context context;

    // RecyclerView recyclerView;
    public ContactAdapter(List<ContactListModel> listData) {
        this.listData = listData;
        this.filteredList = new ArrayList<>(listData);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.rv_contact_content, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ContactListModel myListData = listData.get(position);
        holder.textView.setText(listData.get(position).getName());
        if(listData.get(position).getImage() != null){
            holder.imageView.setImageBitmap(listData.get(position).getImage());
        }else {

        }

        holder.llContacts.setOnClickListener(v -> {
            Intent editIntent = new Intent(Intent.ACTION_EDIT);
            editIntent.setDataAndType(myListData.getUri(), ContactsContract.Contacts.CONTENT_ITEM_TYPE);
            context.startActivity(editIntent);

        });

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public LinearLayout llContacts;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.ivContactImg);
            this.textView = itemView.findViewById(R.id.tvContactName);
            this.llContacts = itemView.findViewById(R.id.llContacts);
        }
    }

  public void updateData(List<ContactListModel> newList){
        listData.clear();
        listData.addAll(newList);
        notifyDataSetChanged();
  }
}
