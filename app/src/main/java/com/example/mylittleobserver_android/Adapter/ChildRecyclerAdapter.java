package com.example.mylittleobserver_android.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylittleobserver_android.Model.InsideItem;
import com.example.mylittleobserver_android.R;

import java.util.ArrayList;

public class ChildRecyclerAdapter extends RecyclerView.Adapter<ChildRecyclerAdapter.ViewHolder> {

    ArrayList<InsideItem> items;

    public ChildRecyclerAdapter(ArrayList<InsideItem> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView alarmTitle;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmTitle = itemView.findViewById(R.id.alarm_title);
            time = itemView.findViewById(R.id.date);

        }
    }

    @NonNull
    @Override
    public ChildRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inside_recyclerview_layout, parent, false);
        ChildRecyclerAdapter.ViewHolder vh = new ChildRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChildRecyclerAdapter.ViewHolder holder, int position) {
        holder.alarmTitle.setText(items.get(position).getAlarmTitle());
        holder.time.setText(items.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
