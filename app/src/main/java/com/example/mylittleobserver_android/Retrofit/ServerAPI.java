package com.example.mylittleobserver_android.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServerAPI {
    @GET("api/v1/posts/1") // API가 어떤걸 보내주느냐에 따라서 달라짐
    Call<Object> getTest(); // 지금 당장은 postId라는 것을 받아온것이지만 서버에서 정한 어떤걸 받아야함

    @GET("api/v1/posts/{id}")
    Call<Object> listRepos(@Path("id")int id);
}
