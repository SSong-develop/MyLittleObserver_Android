package com.example.mylittleobserver_android.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mylittleobserver_android.Activities.MainActivity;
import com.example.mylittleobserver_android.Model.User;
import com.example.mylittleobserver_android.R;

public class Main_Fragment extends Fragment {
    TextView test1;
    Handler handler;
    Toolbar toolbar;
    MainActivity activity;
    User loginUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        TextView userName = (TextView) root.findViewById(R.id.username);
        TextView _mloName = root.findViewById(R.id.mloname);
        ImageView Profile = (ImageView) root.findViewById(R.id.profile);
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
        _mloName.setText(mloName);
        userName.setTextColor(Color.parseColor(color));
        return root;
    }
}