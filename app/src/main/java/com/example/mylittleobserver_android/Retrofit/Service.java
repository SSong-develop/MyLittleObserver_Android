package com.example.mylittleobserver_android.Retrofit;

import com.example.mylittleobserver_android.Model.User;
import com.example.mylittleobserver_android.Model.userSaveRequestDto;

import java.net.URL;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Service {

    // http://ipAddress:portNum/users
    public static final String URL = "http://ec2-15-165-113-25.ap-northeast-2.compute.amazonaws.com:8080/";

    @GET("user")
    Call<ResponseBody> getUser(@Query("mloId") int mloId);
    @GET("api/v1/users")
    Call<List<User>> findAll();

    @POST("api/v1/users")
    Call<ResponseBody> register(@Body userSaveRequestDto userSaveRequestDto);

    // url 동적으로 줘야함
    @POST()
    Call<ResponseBody> mloRegister();

    @GET
    Call<ResponseBody> login(@Url String url);
    @GET
    Call<ResponseBody> getAlarm(@Url String url);
    @GET
    Call<ResponseBody> getAlarmStatus(@Url String url);
    @GET
    Call<ResponseBody> getMloList(@Url String url);

    // http://ipAddress:portNum/users?mloId = mloId 이렇게 데이터를 가져오면 될거같음

    // http://ipAddress:portNum/user
    // http://ipAddress:portNum/register
    // http://ipAddress:portNum/login
}