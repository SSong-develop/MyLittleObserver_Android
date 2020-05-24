package com.example.mylittleobserver_android.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
// 이 클래스에서 하는 역할
// retrofit 객체를 생성해주는 class
// retrofit 객체를 선언
// getRetrofit() Method를 통해서 base_url을 통한 retrofit객체를 생성한다

public class RetrofitClient {
    // 2, client 설정
    // http://192.168.0.10:8080
    public static Retrofit retrofit;

    private static final String BASE_URL = "http://192.168.43.156:8080/";

    // This public static method will return Retrofit client
    // anywhere in the application
    public static Retrofit getRetrofit() { // Retrofit 객체를 생성

        if(retrofit == null){

            // Defining the Retrofit using Builder
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
