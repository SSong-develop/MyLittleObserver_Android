package com.example.mylittleobserver_android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.mylittleobserver_android.Activities.MainActivity;
import com.example.mylittleobserver_android.Model.User;
import com.example.mylittleobserver_android.R;
import com.example.mylittleobserver_android.Retrofit.RetrofitClient;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class Main_Fragment extends Fragment {
    TextView test1;
    Handler handler;
    Toolbar toolbar;
    MainActivity activity;
    User loginUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity)getActivity();
    }

    // http 통신 필요
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_main,container,false);
        TextView userName = (TextView)root.findViewById(R.id.username);
        ImageView Profile = (ImageView)root.findViewById(R.id.profile);
        toolbar = root.findViewById(R.id.main_toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(" ");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white);

        Bundle bundle = getArguments();
        String name = bundle.getString("userName");
        String mloName = bundle.getString("mloName");
        Long mloId = bundle.getLong("mloId");


        String color = "#000000";

        userName.setText(name);
        userName.setTextColor(Color.parseColor(color));
        return root;
    }


    /* class myThread extends Thread {
        // 3.Thread를 이용한 네트워크 호출
        @Override
        public void run() {
            Call<Object> getTest = RetrofitClient.getApiService().getTest();
            try{
                Bundle bun = new Bundle();
                bun.putString("DATA",getTest.execute().body().toString());
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            } catch(IOException e){
                e.printStackTrace();
            }

        }
    }*/
}

/* MLO NUMBER CHECK
private void mlo_check() {
        String mlo = mlo_num.getText().toString();
        mlo = mlo.trim();
        if(mlo.matches("")){
            Toast.makeText(this, "Please enter MLO_NUMBER", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(AddDeviceActivity.this,MainActivity.class);
            intent.putExtra("deviceNumber",mlo_num.getText().toString());
            // 여기서 mlo_num을 들고 나가야 한다.
            startActivity(intent);
            finish();
        }
    }
 */