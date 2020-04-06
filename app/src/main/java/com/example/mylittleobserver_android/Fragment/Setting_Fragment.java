package com.example.mylittleobserver_android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mylittleobserver_android.Activities.MainActivity;
import com.example.mylittleobserver_android.R;

public class Setting_Fragment extends Fragment {
    MainActivity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_setting,container,false);

        //toolbar
        Toolbar tb = (Toolbar)root.findViewById(R.id.settingtoolbar_fragment);
        activity.setSupportActionBar(tb);
        activity.getSupportActionBar().setTitle("설정");

        return root;
    }
}
