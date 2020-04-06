package com.example.mylittleobserver_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.example.mylittleobserver_android.R;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;


public class AddDeviceActivity extends AppCompatActivity {
    BluetoothSPP bt;
    TextView testText;
    AppCompatImageButton Device_write;
    AppCompatImageButton Wifi_connect;
    AppCompatImageButton Bluetooth_connect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        // view
        Wifi_connect = (AppCompatImageButton)findViewById(R.id.wifi_connect);
        Bluetooth_connect = (AppCompatImageButton)findViewById(R.id.bluetooth_connect);
        Device_write = findViewById(R.id.device);
        testText = findViewById(R.id.testText);

        // toolbar
        Toolbar tb = (Toolbar)findViewById(R.id.AddDeviceToolbar);
        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("기기 등록");

        // wifi connect
        Wifi_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect_Wifi();
            }
        });
        // bluetooth connect
        Bluetooth_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect_Bluetooth();
            }
        });
        // add_device
        Device_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect_Direct();
            }
        });

    }

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

    private void Connect_Wifi() {
        Toast.makeText(this, "WIFI 연결", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddDeviceActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void Connect_Bluetooth() {
        Toast.makeText(this, "Bluetooth 연결", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddDeviceActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void Connect_Direct() {
        Toast.makeText(this, "Direct 연결", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddDeviceActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
