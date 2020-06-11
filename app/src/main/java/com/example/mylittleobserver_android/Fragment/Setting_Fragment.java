package com.example.mylittleobserver_android.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mylittleobserver_android.Activities.MainActivity;
import com.example.mylittleobserver_android.R;
import com.kyleduo.switchbutton.SwitchButton;

import static android.content.Context.MODE_PRIVATE;

public class Setting_Fragment extends Fragment {
    MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        //toolbar
        Toolbar tb = (Toolbar) root.findViewById(R.id.settingtoolbar_fragment);
        activity.setSupportActionBar(tb);
        activity.getSupportActionBar().setTitle("설정");

        // View
        SwitchButton heartSwitchButton = root.findViewById(R.id.heartrateswitch);
        SwitchButton decibelSwitchButton = root.findViewById(R.id.decibelswitch);
        SwitchButton tumbleSwitchButton = root.findViewById(R.id.slidingswitch);

        // SharedPreference
        SharedPreferences heartPref = activity.getSharedPreferences("heart",MODE_PRIVATE);
        boolean heartSwitch = heartPref.getBoolean("heart",false);
        heartSwitchButton.setChecked(heartSwitch);

        SharedPreferences decibelPref = activity.getSharedPreferences("decibel",MODE_PRIVATE);
        boolean decibelSwitch = decibelPref.getBoolean("decibel",false);
        decibelSwitchButton.setChecked(decibelSwitch);

        SharedPreferences tumblePref = activity.getSharedPreferences("tumble",MODE_PRIVATE);
        boolean tumbleSwitch = tumblePref.getBoolean("tumble",false);
        tumbleSwitchButton.setChecked(tumbleSwitch);

        // 여기만 좀 생각~~
        heartSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(activity, "심박수 알림 ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "심박수 알림 Off", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences heartSetting = activity.getSharedPreferences("heart",MODE_PRIVATE);
                SharedPreferences.Editor heartEditor = heartSetting.edit();
                heartEditor.putBoolean("heart",isChecked);
                heartEditor.commit();
            }
        });
        decibelSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(activity, "데시벨 알림 ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "데시벨 알림 Off", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences decibelSetting = activity.getSharedPreferences("decibel",MODE_PRIVATE);
                SharedPreferences.Editor decibelEditor = decibelSetting.edit();
                decibelEditor.putBoolean("decibel",isChecked);
                decibelEditor.commit();
            }
        });
        tumbleSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(activity, "넘어짐 알림 ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "넘어짐 알림 Off", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences tumbleSetting = activity.getSharedPreferences("tumble",MODE_PRIVATE);
                SharedPreferences.Editor tumbleEditor = tumbleSetting.edit();
                tumbleEditor.putBoolean("tumble",isChecked);
                tumbleEditor.commit();
            }
        });
        return root;
    }
}
