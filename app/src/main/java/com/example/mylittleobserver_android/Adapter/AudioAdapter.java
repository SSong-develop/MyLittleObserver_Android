package com.example.mylittleobserver_android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylittleobserver_android.Model.MainRecyclerViewItem;
import com.example.mylittleobserver_android.R;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    // 커스텀 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int pos);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;
    private OnItemLongClickListener mLongListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongListener = listener;
    }

    private ArrayList<MainRecyclerViewItem> mlist = null;
    Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView heartrate;
        TextView decibel;
        TextView warningword;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            heartrate = (TextView) itemView.findViewById(R.id.heartrate_number);
            decibel = (TextView) itemView.findViewById(R.id.decibel_number);
            warningword = (TextView) itemView.findViewById(R.id.warningword);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(v, pos);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mLongListener.onItemLongClick(v, pos);
                    }
                    return true;
                }
            });
        }
    }

    public AudioAdapter(ArrayList<MainRecyclerViewItem> list) {
        this.mlist = list;
    }

    @NonNull
    @Override
    public AudioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recyclerview_layout, parent, false);
        AudioAdapter.ViewHolder vh = new AudioAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AudioAdapter.ViewHolder holder, int position) {
        MainRecyclerViewItem item = mlist.get(position);
        String Title = item.getTitle();
        holder.title.setText(Title);
        String Date = item.getDate();
        holder.date.setText(Date);
        String Heartrate = item.getHeartRate();
        holder.heartrate.setText(Heartrate);
        String Decibel = item.getDecibel();
        holder.decibel.setText(Decibel);
        String Word = item.getWarningWord();
        holder.warningword.setText(Word);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public String getTitle(int position) {
        MainRecyclerViewItem item = mlist.get(position);
        String title = item.getTitle();
        return title;
    }

}