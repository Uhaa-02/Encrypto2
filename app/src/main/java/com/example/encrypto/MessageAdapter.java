package com.example.encrypto;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context context;
    ArrayList<MessageDataModel> arrayList;
    SharedPreferences sharedPreferences;

    public MessageAdapter(Context context, ArrayList<MessageDataModel> arrayList, SharedPreferences sharedPreferences){
        this.context = context;
        this.arrayList = arrayList;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public int getItemViewType(int position) {
        String currentUser = sharedPreferences.getString("userName", null);
        if (currentUser != null && currentUser.equals(arrayList.get(position).getSender())) {
            return 0; // sender
        } else {
            return 1; // receiver
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(context).inflate(R.layout.each_message_for_sender, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.each_message_for_receiver, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.msg.setText(arrayList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message);
        }
    }
}
