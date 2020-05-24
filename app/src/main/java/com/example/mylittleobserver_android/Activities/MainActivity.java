package com.example.mylittleobserver_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.mylittleobserver_android.Fragment.Audio_Fragment;
import com.example.mylittleobserver_android.Fragment.Main_Fragment;
import com.example.mylittleobserver_android.Fragment.ManageDevice_Fragment;
import com.example.mylittleobserver_android.Fragment.Setting_Fragment;
import com.example.mylittleobserver_android.Model.Mlos;
import com.example.mylittleobserver_android.Model.User;
import com.example.mylittleobserver_android.R;
import com.example.mylittleobserver_android.Retrofit.RetrofitClient;
import com.example.mylittleobserver_android.Retrofit.Service;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Bottom navigation fragment
    Main_Fragment mainfragment = new Main_Fragment();
    Audio_Fragment audiofragment = new Audio_Fragment();
    Setting_Fragment settingfragment = new Setting_Fragment();
    ManageDevice_Fragment manageDevice_Fragment = new ManageDevice_Fragment();

    String url = "http://ec2-15-165-113-25.ap-northeast-2.compute.amazonaws.com:8080/";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        // getIntent
        // userName, mloName, mloId 이렇게 넘어온다
        Intent intent = getIntent();
        String userName = intent.getExtras().getString("userName");
        String mloName = intent.getExtras().getString("mloName");
        Long mloId = intent.getExtras().getLong("mloId");

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,mainfragment).commitAllowingStateLoss();

        // Main Fragment에게 전달하는 bundle
        Bundle bundle1 = new Bundle();
        bundle1.putString("userName",userName);
        bundle1.putString("mloName",mloName);
        bundle1.putLong("mloId",mloId);
        mainfragment.setArguments(bundle1);

        // Audio Fragment에게 전달 bundle
        Bundle bundle = new Bundle();
        bundle.putString("mloName",mloName);
        bundle.putString("userName",userName);
        bundle.putLong("mloId",mloId);
        audiofragment.setArguments(bundle);

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
