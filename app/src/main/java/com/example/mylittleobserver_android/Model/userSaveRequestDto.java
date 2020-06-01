package com.example.mylittleobserver_android.Model;

import com.google.gson.annotations.SerializedName;

public class userSaveRequestDto {
    @SerializedName("username")
    String username;

    public userSaveRequestDto(String username) {
    this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
