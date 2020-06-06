package com.example.mylittleobserver_android.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.mylittleobserver_android.R;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(),1500);
    }

    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(SplashActivity.this,AddDeviceActivity.class));
            finish();
        }
    }
}
