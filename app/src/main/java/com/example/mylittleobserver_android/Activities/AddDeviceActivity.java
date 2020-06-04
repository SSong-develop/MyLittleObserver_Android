package com.example.mylittleobserver_android.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mylittleobserver_android.Model.User;
import com.example.mylittleobserver_android.Model.userSaveRequestDto;
import com.example.mylittleobserver_android.R;
import com.example.mylittleobserver_android.Retrofit.Service;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Map;

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

    private MaterialButton acceptbtn;
    private MaterialButton registerbtn;
    private TextInputEditText userName_et;
    private TextInputEditText userId_et;

    private Handler handler;

    private Retrofit loginRetrofit;
    private Service service;
    private Call<User> users;


    // 와이파이를 이용한 네트워크 연결 확정
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        // view
        acceptbtn = findViewById(R.id.acceptbtn);
        registerbtn = findViewById(R.id.register);
        userName_et = findViewById(R.id.username_et);
        userId_et = findViewById(R.id.mloId_et);


        // toolbar
        Toolbar tb = (Toolbar)findViewById(R.id.AddDeviceToolbar);
        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("MLO");

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.equals(userName_et.getText().toString(),"")){
                    Toast.makeText(AddDeviceActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.equals(userId_et.getText().toString(),"")){
                    Toast.makeText(AddDeviceActivity.this, "Please enter your device number", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginRetrofit = new Retrofit.Builder()
                            .baseUrl(BASEURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    service = loginRetrofit.create(Service.class);
                    String url = BASEURL + "api/v1/users/" + userName_et.getText().toString();
                    Call<ResponseBody> call = service.login(url);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try{
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
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

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
        View dialogView = getLayoutInflater().inflate(R.layout.register_dialog,null);
        TextInputEditText registerName = dialogView.findViewById(R.id.register_user);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        builder.setTitle("REGISTER").setMessage("사용자 닉네임을 적어주세요");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userSaveRequestDto user = new userSaveRequestDto(registerName.getText().toString());
                Log.d("TAGG!",user.getUsername());
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
                                if (response.code() == 400){
                                    Toast.makeText(AddDeviceActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else{
                                    Toast.makeText(AddDeviceActivity.this, "BAD GATE"+"Error Code : "+response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                String result = response.body().string();
                                JSONObject jsonObject = new JSONObject(result);
                                Toast.makeText(AddDeviceActivity.this, jsonObject.getString("message")+"   "+"회원 아이디 : "+jsonObject.getInt("id"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

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
        switch (item.getItemId()){
            case android.R.id.home :{
                finish();
                overridePendingTransition(R.anim.right_out_activity,R.anim.not_move_activity);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
