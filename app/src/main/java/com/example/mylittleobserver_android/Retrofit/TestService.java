package com.example.mylittleobserver_android.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestService {
    @GET("/api/v1/posts/1")
    Call<Object> getTest();
}
