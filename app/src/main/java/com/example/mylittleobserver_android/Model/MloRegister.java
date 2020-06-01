package com.example.mylittleobserver_android.Model;

import com.google.gson.annotations.SerializedName;

public class MloRegister {
    @SerializedName("userName")
    String userName;
    mloSaveRequestDto mloSaveRequestDto;

    public MloRegister(String userName, com.example.mylittleobserver_android.Model.mloSaveRequestDto mloSaveRequestDto) {
        this.userName = userName;
        this.mloSaveRequestDto = mloSaveRequestDto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public com.example.mylittleobserver_android.Model.mloSaveRequestDto getMloSaveRequestDto() {
        return mloSaveRequestDto;
    }

    public void setMloSaveRequestDto(com.example.mylittleobserver_android.Model.mloSaveRequestDto mloSaveRequestDto) {
        this.mloSaveRequestDto = mloSaveRequestDto;
    }
}
