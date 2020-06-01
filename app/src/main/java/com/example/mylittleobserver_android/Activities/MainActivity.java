package com.example.mylittleobserver_android.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Graduate Project
 * MLO_ANDROID
 * DEVELOP PERIOD
 * 2019.12.00 ~ 2020.06.12
 */
public class MainActivity extends AppCompatActivity {

    // api/v1/users/{userName} => 유저의 mlo기계 확인
    // Bottom navigation fragment
    Main_Fragment mainfragment = new Main_Fragment();
    Audio_Fragment audiofragment = new Audio_Fragment();
    Setting_Fragment settingfragment = new Setting_Fragment();
    ManageDevice_Fragment manageDevice_Fragment = new ManageDevice_Fragment();

    // URL
    String URL = "http://ec2-15-165-113-25.ap-northeast-2.compute.amazonaws.com:8080/";

    // View Instance
    private DrawerLayout mDrawerLayout;
    private Context context = this;

    // dialog Instance
    TextInputEditText registerName;

    // Retrofit
    private Retrofit mloListRetrofit;
    private Service listService;
    private Call<ResponseBody> call;
    private ArrayList<JSONObject> arrayListJsonObject;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        // View
        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // title없애기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        actionBar.setHomeAsUpIndicator(R.drawable.fast_rewind_white); //뒤로가기 버튼 아이콘

        mDrawerLayout = findViewById(R.id.drawerlayout);

        // getIntent

        // userName, mloName, mloId 이렇게 넘어온다
        Intent intent = getIntent();
        String userName = intent.getExtras().getString("userName");
        String mloName = intent.getExtras().getString("mloName");
        Long mloId = intent.getExtras().getLong("mloId");


        // Navigation DrawerView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.mloRegister){
                    View dialogView = getLayoutInflater().inflate(R.layout.register_mlo_register,null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);
                    builder.setTitle("기계등록").setMessage("사용자 닉네임을 적어주세요");
                    builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url = URL + "api/v1/users/"+ userName +"/mlos/";
                            Log.v("FuckURL",url);
                            registerName = dialogView.findViewById(R.id.register_mlo);
                            if(TextUtils.equals(registerName.getText().toString(),"")){
                                Toast.makeText(context, "[실패] 닉네임을 적어주세요", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else if(!TextUtils.equals(registerName.getText().toString(),userName)){
                                Toast.makeText(context, "[실패] 유저이름과 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                Retrofit mloRegister = new Retrofit.Builder()
                                        .baseUrl(url)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                Service mloService = mloRegister.create(Service.class);
                                Call<ResponseBody> call = mloService.mloRegister(url);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "취소!!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                return true;
            }
        });

        // Retrofit
        String url = URL + "api/v1/users/" + userName+"/";
        mloListRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        listService = mloListRetrofit.create(Service.class);
        call = listService.getMloList(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                } else {
                    try{
                        String result = response.body().string();
                        JSONArray jsonArray = new JSONArray(result);
                        // jsonArray가 2개 이상일 경우 반복문으로 변경해준다.
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONArray mloList = jsonObject.getJSONArray("mlos");
                        for(int i = 0 ; i<mloList.length();i++){
                            JSONObject jsonObject1 = mloList.getJSONObject(i);
                            // arrayListJsonObject.add(jsonObject1);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        // Main Fragment__bundle
        Bundle bundle1 = new Bundle();
        bundle1.putString("userName",userName);
        bundle1.putString("mloName",mloName);
        bundle1.putLong("mloId",mloId);
        mainfragment.setArguments(bundle1);

        // Audio Fragment__bundle
        Bundle bundle = new Bundle();
        bundle.putString("mloName",mloName);
        bundle.putString("userName",userName);
        bundle.putLong("mloId",mloId);
        audiofragment.setArguments(bundle);

        // bottomNavigationView에 따른 화면 전환

        getSupportFragmentManager().beginTransaction().replace(R.id.relative_layout,mainfragment).commitAllowingStateLoss();

        // BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch(Item.getItemId())
                {
                    // before : R.id.frame_layout
                    case R.id.menu_one: {
                        transaction.replace(R.id.relative_layout,mainfragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu_two: {
                        transaction.replace(R.id.relative_layout,audiofragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu_three : {
                        transaction.replace(R.id.relative_layout, manageDevice_Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu_four : {
                        transaction.replace(R.id.relative_layout,settingfragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }
}
