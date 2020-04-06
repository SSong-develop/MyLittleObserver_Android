package com.example.mylittleobserver_android.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.mylittleobserver_android.Fragment.Audio_Fragment;
import com.example.mylittleobserver_android.Fragment.Main_Fragment;
import com.example.mylittleobserver_android.Fragment.ManageDevice_Fragment;
import com.example.mylittleobserver_android.Fragment.Setting_Fragment;
import com.example.mylittleobserver_android.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Main_Fragment mainfragment = new Main_Fragment();
    Audio_Fragment audiofragment = new Audio_Fragment();
    Setting_Fragment settingfragment = new Setting_Fragment();
    ManageDevice_Fragment manageDevice_Fragment = new ManageDevice_Fragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,mainfragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch(Item.getItemId())
                {
                    case R.id.menu_one: {
                        transaction.replace(R.id.framelayout,mainfragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu_two: {
                        transaction.replace(R.id.framelayout,audiofragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu_three : {
                        transaction.replace(R.id.framelayout, manageDevice_Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu_four : {
                        transaction.replace(R.id.framelayout,settingfragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }
}
