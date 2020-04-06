package com.example.mylittleobserver_android.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mylittleobserver_android.R;
import com.example.mylittleobserver_android.Retrofit.ServerAPI;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class Main_Fragment extends Fragment {

    // http 통신 필요
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_main,container,false);
        TextView userName = (TextView)root.findViewById(R.id.username);
        ImageView Profile = (ImageView)root.findViewById(R.id.profile);
        final TextView test1 = (TextView)root.findViewById(R.id.tests);


        return root;
    }
}
