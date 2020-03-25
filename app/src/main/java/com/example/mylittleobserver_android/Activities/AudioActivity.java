package com.example.mylittleobserver_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylittleobserver_android.Adapter.AudioAdapter;
import com.example.mylittleobserver_android.Model.RecyclerViewItem;
import com.example.mylittleobserver_android.R;

import java.util.ArrayList;

public class AudioActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AudioAdapter adapter;
    ImageView Backbtn;

    private ArrayList<RecyclerViewItem> mlist = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home :
                finish();
                overridePendingTransition(R.anim.sliding_up,R.anim.sliding_down);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_audio);

        Toolbar tb = (Toolbar)findViewById(R.id.audioToolbar);
        // 리사이클러뷰가 쫘악 있다가 클릭이벤트가 나오면 그때 밑에 있는 그 음악 재생 레이아웃이 나올 수 있도록 하면 더 좋을거같음
        // 음파 찍는 이퀄라이저도 구현 생각
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

        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("녹음파일 목록");
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new AudioAdapter(mlist);
        adapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(AudioActivity.this,AudioPlayerActivity.class);
                intent.putExtra("SelectedTitle",adapter.getTitle(pos));
                startActivity(intent);

                overridePendingTransition(R.anim.sliding_up,R.anim.sliding_up);
            }
        });
        adapter.setOnItemLongClickListener(new AudioAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

}

