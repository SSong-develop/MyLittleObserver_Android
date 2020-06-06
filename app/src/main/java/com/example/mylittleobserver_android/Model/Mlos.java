package com.example.mylittleobserver_android.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mlos implements Parcelable {
    Long mloId;
    String mloName;

    public Mlos(Parcel source) {
        this.mloId = source.readLong();
        this.mloName = source.readString();

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mloId);
        dest.writeString(mloName);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator<Mlos> CREATOR = new Parcelable.Creator<Mlos>(){

        @Override
        public Mlos createFromParcel(Parcel source) {
            return new Mlos(source);
        }

        @Override
        public Mlos[] newArray(int size) {
            return new Mlos[size];
        }
    };
}
