package com.example.mylittleobserver_android.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
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
    private Context context;

    public MainRecyclerAdapter(ArrayList<Section> sectionList){
        this.sectionList = sectionList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;
        RecyclerView childRecyclerView;
        TextView buttonViewOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.sectionName);
            childRecyclerView = itemView.findViewById(R.id.child_recyclerview);
            buttonViewOption = itemView.findViewById(R.id.textViewOptions);

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
        context = parent.getContext();
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

        holder.buttonViewOption.setOnClickListener(v -> {
            //creating a popup menu
            PopupMenu popup;
            popup = new PopupMenu(context, holder.buttonViewOption);
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            //handle menu1 click
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("정말 삭제 하시나요?");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            androidx.appcompat.app.AlertDialog dialog = builder.create();
                            dialog.show();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
        });

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
