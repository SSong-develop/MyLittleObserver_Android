package com.example.mylittleobserver_android.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylittleobserver_android.Model.InsideItem;
import com.example.mylittleobserver_android.Model.MainRecyclerViewItem;
import com.example.mylittleobserver_android.Model.Section;
import com.example.mylittleobserver_android.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {
    // 커스텀 리스너 인터페이스
    public interface OnItemClickListener
    {
        void onItemClick(View v,int pos);
    }
    public interface OnItemLongClickListener
    {
        void onItemLongClick(View v,int pos);
    }

    // 리스너 객체 참조를 저장하는 변수
    private MainRecyclerAdapter.OnItemClickListener mListener = null;
    private MainRecyclerAdapter.OnItemLongClickListener mLongListener = null;
    public void setOnItemClickListener(MainRecyclerAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }
    public void setOnItemLongClickListener(MainRecyclerAdapter.OnItemLongClickListener listener)
    {
        this.mLongListener = listener;
    }
    ArrayList<Section> sectionList;

    public MainRecyclerAdapter(ArrayList<Section> sectionList){
        this.sectionList = sectionList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;
        RecyclerView childRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.sectionName);
            childRecyclerView = itemView.findViewById(R.id.child_recyclerview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemClick(v,pos);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                    {
                        mLongListener.onItemLongClick(v,pos);
                    }
                    return true;
                }
            });
        }
    }
    @NonNull
    @Override
    public MainRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_row,parent,false);
        MainRecyclerAdapter.ViewHolder vh = new MainRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerAdapter.ViewHolder holder, int position) {
        Section section = sectionList.get(position);
        String sectionName = section.getSectionName();
        ArrayList<InsideItem> items = section.getSectionItems();

        holder.sectionName.setText(sectionName);

        ChildRecyclerAdapter childRecyclerAdapter = new ChildRecyclerAdapter(items);
        holder.childRecyclerView.setAdapter(childRecyclerAdapter);

    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public String getTitle(int position)
    {
        String title = sectionList.get(position).getSectionName();
        return title;
    }
}
