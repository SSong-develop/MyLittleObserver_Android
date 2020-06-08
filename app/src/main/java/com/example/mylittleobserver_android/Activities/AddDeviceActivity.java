package com.example.mylittleobserver_android.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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

    private static int userId = 0;

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
                            try {
                                String result1 = response.body().string();
                                if(TextUtils.equals(result1,"No mlo")){
                                    // response.body().string = No mlo
                                    // No mlo라는 녀석이 보내진다면 ArrayList를 생성하지 않는다~
                                    // 이 부분을 판단해 줄 녀석이 필요함
                                    ArrayList<Mlos> mlosArrayList = new ArrayList<>();
                                    Intent intent = new Intent(AddDeviceActivity.this,MainActivity.class);
                                    intent.putExtra("userId",userId);
                                    intent.putExtra("userName",userName_et.getText().toString());
                                    intent.putParcelableArrayListExtra("mloList",mlosArrayList);
                                    startActivity(intent);
                                    finish();
                                } else if(response.body() == null){
                                    Toast.makeText(AddDeviceActivity.this, "등록된 회원이 아닙니다. 회원가입을 해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                } else{
                                    // 성공
                                    try{
                                        JSONArray jsonArray = new JSONArray(result1);
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
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(AddDeviceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

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
                                    // 원래 error : code 500 Bad Gate 표시나옴
                                    // Toast 가독성을 위해서 위치를 변경
                                    Toast toast = Toast.makeText(AddDeviceActivity.this,"error : code 500 Bad Gate" + response.code(),Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                                    toast.show();
                                }
                            } else {
                                String result = response.body().string();
                                JSONObject jsonObject = new JSONObject(result);
                                userId = jsonObject.getInt("id");
                                Log.d("FuckLog!!!", String.valueOf(userId));
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
