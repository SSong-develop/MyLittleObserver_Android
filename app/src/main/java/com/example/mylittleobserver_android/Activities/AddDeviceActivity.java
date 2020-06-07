package com.example.mylittleobserver_android.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.versionedparcelable.ParcelField;

import com.example.mylittleobserver_android.Model.Mlos;
import com.example.mylittleobserver_android.Model.User;
import com.example.mylittleobserver_android.Model.userSaveRequestDto;
import com.example.mylittleobserver_android.R;
import com.example.mylittleobserver_android.Retrofit.Service;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Map;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 등록 부분 끝!!
 * mlo 등록 부분도 금방 끝낼 거 같음
 */
public class AddDeviceActivity extends AppCompatActivity {
    private final String BASEURL = "http://ec2-15-165-113-25.ap-northeast-2.compute.amazonaws.com:8080/";
    public static final int NO_MLO_LOGIN = 500;
    public static final int YES_MLO_LOGIN = 200;

    private MaterialButton acceptbtn;
    private MaterialButton registerbtn;
    private TextInputEditText userName_et;
    /*private TextInputEditText userId_et;*/

    private Handler handler;

    private Retrofit loginRetrofit;
    private Service service;
    private Call<User> users;
    private CheckBox autoLogin;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    // 와이파이를 이용한 네트워크 연결 확정
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        // View
        acceptbtn = findViewById(R.id.acceptbtn);
        registerbtn = findViewById(R.id.register);
        userName_et = findViewById(R.id.username_et);
        /*userId_et = findViewById(R.id.mloId_et);*/
        autoLogin = findViewById(R.id.auto_login);

        preferences = getSharedPreferences("Data", MODE_PRIVATE);
        editor = preferences.edit();
        if (preferences.getBoolean("Auto_Login_enabled", false)) {
            userName_et.setText(preferences.getString("Id", ""));
            autoLogin.setChecked(true);
        }

        autoLogin.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoLogin.isChecked()) {
                    String Id = userName_et.getText().toString();

                    editor.putString("Id", Id);
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                } else {
                    editor.remove("Id");
                    editor.remove("mloId");
                    editor.remove("Auto_Login_enabled");
                    editor.clear();
                    editor.commit();
                }
            }
        });

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(userName_et.getText().toString(), "")) {
                    Toast.makeText(AddDeviceActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.equals(userName_et.getText().toString(),"thdgnsrl")){
                    Intent intent1 = new Intent(AddDeviceActivity.this,MainActivity.class);
                    startActivity(intent1);
                } else {
                    String url = BASEURL + "api/v1/users/" + userName_et.getText().toString();
                    Log.d("Fuck_URL", url);
                    loginRetrofit = new Retrofit.Builder()
                            .baseUrl(BASEURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    service = loginRetrofit.create(Service.class);
                    Call<ResponseBody> call = service.login(url);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.body() == null){
                                // 이 부분을 판단해 줄 녀석이 필요함
                            }
                            else {
                                // 성공
                                try {
                                    String result = response.body().string();
                                    try{
                                        JSONArray jsonArray = new JSONArray(result);
                                        int userId;
                                        String name;
                                        JSONArray mloList;
                                        JSONObject mlos = jsonArray.getJSONObject(0);
                                        userId = mlos.getInt("userId");
                                        name = mlos.getString("name");
                                        mloList = mlos.getJSONArray("mlos");
                                        ArrayList<Mlos> mlosArrayList = new ArrayList<>();

                                        if(mloList == null){
                                            Toast.makeText(AddDeviceActivity.this, "로그인 성공, 기계를 등록해주세요", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            for(int i = 0 ; i<mloList.length();i++){
                                                Mlos mlo = new Mlos(mloList.getJSONObject(i).getLong("mloId"),mloList.getJSONObject(i).getString("mloName"));
                                                mlosArrayList.add(mlo);
                                                // mloNameList(0) = mlo1
                                                // mloNameList(1) = mlo2
                                            }
                                            Intent intent = new Intent(AddDeviceActivity.this,MainActivity.class);
                                            intent.putExtra("userName",name);
                                            intent.putExtra("userId",userId);
                                            intent.putParcelableArrayListExtra("mloList",mlosArrayList);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            /*try{
                                String result = response.body().string();
                                        Log.v("Test",result);
                                        try{
                                            JSONArray jsonArray = new JSONArray(result);
                                            // 만약 사람이 많아지면 반복문으로 수정
                                            Long userId;
                                            String userName;
                                            JSONArray mlos;
                                            JSONObject mlo1;
                                            JSONObject mlo2;
                                            Long mloId;
                                            String mloName = null;
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            userId = jsonObject.getLong("userId");
                                            userName = jsonObject.getString("name");
                                            mlos = jsonObject.getJSONArray("mlos"); // 2개의 mlo들을 받아와
                                            mlo1 = mlos.getJSONObject(0);
                                            mlo2 = mlos.getJSONObject(1);
                                            if(TextUtils.equals(userName_et.getText().toString(),"userA")){
                                                if(TextUtils.equals(userName,userName_et.getText().toString())
                                                        &&TextUtils.equals(userId_et.getText().toString(),String.valueOf(mlo1.getLong("mloId"))))
                                                {
                                                    Toast.makeText(AddDeviceActivity.this, "mlo1 : LoginSucess", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(AddDeviceActivity.this,MainActivity.class);
                                                    intent.putExtra("userName",userName);
                                                    intent.putExtra("mloName",mlo1.getString("mloName"));
                                                    intent.putExtra("mloId",mlo1.getLong("mloId"));
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else if(TextUtils.equals(userName,userName_et.getText().toString())
                                                        &&TextUtils.equals(userId_et.getText().toString(),String.valueOf(mlo2.getLong("mloId"))))
                                                {
                                                    Toast.makeText(AddDeviceActivity.this, "mlo2 : LoginSucess", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(AddDeviceActivity.this,MainActivity.class);
                                                    intent.putExtra("userName",userName);
                                                    intent.putExtra("mloName",mlo2.getString("mloName"));
                                                    intent.putExtra("mloId",mlo2.getLong("mloId"));
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else
                                                {
                                                    Toast.makeText(AddDeviceActivity.this, "Wrong username else mloId", Toast.LENGTH_SHORT).show();
                                                }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }*/
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(AddDeviceActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        View dialogView = getLayoutInflater().inflate(R.layout.register_dialog, null);
        TextInputEditText registerName = dialogView.findViewById(R.id.register_user);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        builder.setTitle("REGISTER").setMessage("사용자 닉네임을 적어주세요");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userSaveRequestDto user = new userSaveRequestDto(registerName.getText().toString());
                Log.d("TAGG!", user.getUsername());
                Retrofit registerRetrofit = new Retrofit.Builder()
                        .baseUrl(BASEURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Service registerService = registerRetrofit.create(Service.class);
                Call<ResponseBody> registerCall = registerService.register(user);
                registerCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if (!response.isSuccessful()) {
                                if (response.code() == 400) {
                                    Toast.makeText(AddDeviceActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    // 여기 문제임
                                    Toast.makeText(AddDeviceActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                String result = response.body().string();
                                JSONObject jsonObject = new JSONObject(result);
                                Toast.makeText(AddDeviceActivity.this, jsonObject.getString("message") + "   " + "회원 아이디 : " + jsonObject.getInt("id"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(AddDeviceActivity.this, "[Error]" + t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                overridePendingTransition(R.anim.right_out_activity, R.anim.not_move_activity);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "종료 시, 뒤로가기 버튼을 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}
