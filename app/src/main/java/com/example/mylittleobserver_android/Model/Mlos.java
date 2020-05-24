package com.example.mylittleobserver_android.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mlos {
    Long mloId;
    String mloName;

    public Mlos() {

    }

    public Mlos(Long mloId, String mloName) {
        this.mloId = mloId;
        this.mloName = mloName;
    }

    public Long getMloId() {
        return mloId;
    }

    public void setMloId(Long mloId) {
        this.mloId = mloId;
    }

    public String getMloName() {
        return mloName;
    }

    public void setMloName(String mloName) {
        this.mloName = mloName;
    }
}
