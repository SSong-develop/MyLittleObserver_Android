package com.example.mylittleobserver_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylittleobserver_android.Adapter.DeviceAdapter;
import com.example.mylittleobserver_android.R;

import gun0912.tedkeyboardobserver.TedKeyboardObserver;

public class AddDeviceActivity extends AppCompatActivity {
    Button AddDevice;
    RecyclerView recyclerView;
    DeviceAdapter adapter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :{
                finish();
                overridePendingTransition(R.anim.right_out_activity,R.anim.not_move_activity);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        // Add Button
        AddDevice = findViewById(R.id.add_device);
        AddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDeviceActivity.this,TestActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // toolbar
        Toolbar tb = (Toolbar)findViewById(R.id.AddDeviceToolbar);
        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("기기 등록");

        // recyclerview
        recyclerView = findViewById(R.id.device_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DeviceAdapter();
        recyclerView.setAdapter(adapter);


    }
}
