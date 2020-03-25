package com.example.mylittleobserver_android.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylittleobserver_android.Activities.AudioActivity;
import com.example.mylittleobserver_android.Activities.AudioPlayerActivity;
import com.example.mylittleobserver_android.Activities.TestActivity;
import com.example.mylittleobserver_android.Adapter.AudioAdapter;
import com.example.mylittleobserver_android.Model.RecyclerViewItem;
import com.example.mylittleobserver_android.R;

import java.util.ArrayList;

public class Audio_Fragment extends Fragment {
    TestActivity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (TestActivity)getActivity();
    }

    private ArrayList<RecyclerViewItem> mlist = new ArrayList<>();
    AudioAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_audio,container,false);

        // toolbar
        Toolbar tb = (Toolbar)root.findViewById(R.id.audioToolbar_fragment);
        activity.setSupportActionBar(tb);
        activity.getSupportActionBar().setTitle("녹음파일 목록");

        // recyclerview dumy data
        for(int i = 0;i<20;i++)
        {
            RecyclerViewItem item = new RecyclerViewItem();
            item.setTitle(String.format("%d번째 제목",i));
            item.setDate(String.format("2020년 %d월 %d일",i,i+1));
            item.setHeartRate(String.format("%d",i));
            item.setDecibel(String.format("%d",i));
            item.setWarningWord(String.format("%d번째 위험",i));
            mlist.add(item);
        }

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(activity).getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new AudioAdapter(mlist);
        adapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(activity, AudioPlayerActivity.class);
                intent.putExtra("SelectedTitle",adapter.getTitle(pos));
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener(new AudioAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });
        recyclerView.setAdapter(adapter);

        return root;
    }
}
