package com.example.mylittleobserver_android.Model;

import com.google.gson.annotations.SerializedName;

public class mloSaveRequestDto {
    @SerializedName("mloName")
    String mloName;

    public mloSaveRequestDto(String mloName) {
        this.mloName = mloName;
    }

    public String getMloName() {
        return mloName;
    }

    public void setMloName(String mloName) {
        this.mloName = mloName;
    }
}
