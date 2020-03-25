package com.example.mylittleobserver_android.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylittleobserver_android.R;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.Viewholder> {

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.predevice_num);
            date = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Loading", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (View)layoutInflater.inflate(R.layout.recyclerview_device_layout,parent,false);
        DeviceAdapter.Viewholder vh = new DeviceAdapter.Viewholder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
