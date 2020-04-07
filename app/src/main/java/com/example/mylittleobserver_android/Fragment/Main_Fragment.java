package com.example.mylittleobserver_android.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mylittleobserver_android.R;
import com.example.mylittleobserver_android.Retrofit.RetrofitClient;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class Main_Fragment extends Fragment {
    TextView test1;
    Handler handler;
    // http 통신 필요
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_main,container,false);
        TextView userName = (TextView)root.findViewById(R.id.username);
        ImageView Profile = (ImageView)root.findViewById(R.id.profile);
        test1 = root.findViewById(R.id.tests);

        new myThread().start();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bun = msg.getData();
                String data = bun.getString("DATA");
                test1.setText(data);
            }
        };
        return root;
    }

    class myThread extends Thread {
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
    }

}
