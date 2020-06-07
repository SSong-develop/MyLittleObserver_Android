package com.example.mylittleobserver_android.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.example.mylittleobserver_android.Model.InsideItem;
import com.example.mylittleobserver_android.Model.MloRegister;
import com.example.mylittleobserver_android.Model.Mlos;
import com.example.mylittleobserver_android.Model.Section;
import com.example.mylittleobserver_android.Model.mloSaveRequestDto;
import com.example.mylittleobserver_android.R;
import com.example.mylittleobserver_android.Retrofit.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
    TextInputEditText registerMloName;

    private boolean _switch = true;

    // mloListRetrofit
    private Retrofit mloListRetrofit;
    private Service listService;
    private Call<ResponseBody> call;
    private ArrayList<JSONObject> arrayListJsonObject;

    // Alarm Retrofit
    private Retrofit alarmRetrofit;
    private Service alarmService;
    private Call<ResponseBody> alarmCall;
    private ArrayList<InsideItem> insideItems; // alarmId , title , date 로 구성
    private ArrayList<Section> sections; // section은 section name과 insideItem으로 구성

    // mloRegister Retrofit
    private Retrofit mloRegisterRetrofit;
    private Service mloRegisterService;
    private Call<ResponseBody> mloRegisterCall;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
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

        // Shared Preference
        // this part need to develop
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // getIntent
        Intent intent = getIntent();
        /*int userId = intent.getExtras().getInt("userId");*/
        String userName = intent.getExtras().getString("userName");
        ArrayList<Mlos> mlosArrayList = intent.getParcelableArrayListExtra("mloList");
        ArrayList<String> mloNameList = new ArrayList<>();
        for (int i = 0; i < mlosArrayList.size(); i++) {
            String mloName = mlosArrayList.get(i).getMloName();
            Log.d("TAG", mloName);
            mloNameList.add(mloName);
        }


        // FCM Token
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = task.getResult().getToken();

                // Log and Toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d("Fucking_FCM_TOKEN", msg);
                /*Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();*/
            }
        });


        // Navigation DrawerView
        // MLO등록 Must do something
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                // mloRegister부분 이곳 조지면 됨
                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();
                // model : mloRegister
                // 구성 : 유저이름(string) , mloSaveRequestDto
                // mloSaveRequestDto 의 구성 : 마리옵이름(string)

                if (id == R.id.mloRegister) {
                    View dialogView = getLayoutInflater().inflate(R.layout.register_mlo_register, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);
                    builder.setTitle("기계등록").setMessage("사용자 닉네임과 기기이름을 적어주세요");
                    builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            registerName = (TextInputEditText) dialogView.findViewById(R.id.register_mlo);
                            registerMloName = (TextInputEditText) dialogView.findViewById(R.id.register_mloName);
                            mloSaveRequestDto _mloSaveRequestDto = new mloSaveRequestDto(registerMloName.getText().toString());
                            String _registerName = registerName.getText().toString();

                            // Exception
                            if (TextUtils.isEmpty(registerName.getText().toString())) {
                                Toast.makeText(context, "[실패]유저이름을 적어주세요", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else if (!TextUtils.equals(registerName.getText().toString(), userName)) {
                                Toast.makeText(context, "[실패]유저이름과 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else if (TextUtils.isEmpty(registerMloName.getText().toString())) {
                                Toast.makeText(context, "[실패]기기 이름을 적어주세요", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                // mloRegister Model
                                MloRegister _mloRegister = new MloRegister(_registerName, _mloSaveRequestDto);

                                // 기기 등록 api 호출
                                mloRegisterRetrofit = new Retrofit.Builder()
                                        .baseUrl(URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                mloRegisterService = mloRegisterRetrofit.create(Service.class);
                                mloRegisterCall = mloRegisterService.mloRegister(_registerName, _mloSaveRequestDto);
                                Log.v("Fuck_register", _registerName);
                                Log.d("Fuck Retrofit", mloRegisterCall.toString());
                                mloRegisterCall.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (!response.isSuccessful()) {
                                            Toast.makeText(context, "[실패]기기등록 실패", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                String result = response.body().string();
                                                JSONObject jsonObject = new JSONObject(result);
                                                int mloId = jsonObject.getInt("mloId");
                                                mlosArrayList.add(new Mlos((long) mloId, registerMloName.getText().toString()));
                                                // Toast 가독성을 위해서 위치를 위로 옮김
                                                Toast toast = Toast.makeText(context, result, Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                                                toast.show();
                                            } catch (IOException | JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
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
                            Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else if (id == R.id.mloSelect) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setIcon(R.drawable.ic_launcher_foreground);
                    alertBuilder.setTitle("기기를 선택하세요");

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            context, android.R.layout.select_dialog_item
                    );
                    for (int i = 0; i < mlosArrayList.size(); i++) {
                        adapter.add(mlosArrayList.get(i).getMloName());
                    }
                    alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertBuilder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String selectedMloName;
                                    selectedMloName = String.valueOf(adapter.getItem(which));
                                    if (which == 0) { // 첫번째 꺼
                                        Bundle bundle = new Bundle();
                                        bundle.putString("userName", userName);
                                        bundle.putString("mloName", selectedMloName);
                                        Long mloId = null;
                                        for (int i = 0; i < mlosArrayList.size(); i++) {
                                            if (TextUtils.equals(selectedMloName, String.valueOf(mlosArrayList.get(i).getMloName()))) {
                                                mloId = Long.valueOf(String.valueOf(mlosArrayList.get(i).getMloId()));
                                            } else {
                                                /*Toast.makeText(context, "기기값이 없습니다.", Toast.LENGTH_SHORT).show();*/
                                            }
                                        }
                                        bundle.putLong("mloId", mloId);
                                        mainfragment.setArguments(bundle);
                                        audiofragment.setArguments(bundle);
                                    } else if (which == 1) { // 두번째 꺼
                                        _switch = false;
                                    }
                                    AlertDialog.Builder innBuilder = new AlertDialog.Builder(context);
                                    innBuilder.setMessage(selectedMloName + "선택하셨습니다");
                                    innBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    innBuilder.show();
                                }
                            });
                    alertBuilder.show();
                }
                return true;
            }
        });
        // Main Fragment__bundle
        Bundle bundle1 = new Bundle();
        bundle1.putString("userName", userName);
        /*bundle1.putString("mloName",mloName);*/
        /*bundle1.putLong("mloId",mloId);*/
        mainfragment.setArguments(bundle1);

        // Audio Fragment__bundle
        Bundle bundle = new Bundle();
        /*bundle.putString("mloName",mloName);*/
        bundle.putString("userName", userName);
        /*bundle.putLong("mloId",mloId);*/
        audiofragment.setArguments(bundle);

        // Retrofit
        // MLO List가져오기
        String url = URL + "api/v1/users/" + userName + "/";
        mloListRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        listService = mloListRetrofit.create(Service.class);
        call = listService.getMloList(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String result = response.body().string();
                        JSONArray jsonArray = new JSONArray(result);
                        // jsonArray가 2개 이상일 경우 반복문으로 변경해준다.
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONArray mloList = jsonObject.getJSONArray("mlos");
                        for (int i = 0; i < mloList.length(); i++) {
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

        // Retrofit
        // MLO AlarmList 가져오기


        // bottomNavigationView에 따른 화면 전환

        getSupportFragmentManager().beginTransaction().replace(R.id.relative_layout, mainfragment).commitAllowingStateLoss();

        // BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (Item.getItemId()) {
                    // before : R.id.frame_layout
                    case R.id.menu_one: {
                        transaction.replace(R.id.relative_layout, mainfragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu_two: {
                        transaction.replace(R.id.relative_layout, audiofragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu_three: {
                        transaction.replace(R.id.relative_layout, manageDevice_Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu_four: {
                        transaction.replace(R.id.relative_layout, settingfragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }

}
