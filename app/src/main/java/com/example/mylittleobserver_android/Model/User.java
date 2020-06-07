package com.example.mylittleobserver_android.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    Long userId;
    String name;
    Mlos mlos;

    public User() {

    }

    public User(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public User(Long userId, String name, Mlos mlos) {
        this.userId = userId;
        this.name = name;
        this.mlos = mlos;
    }

    // 객체를 받아올때 호출
    public User(Parcel in) {
        userId = in.readLong();
        name = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    // 객체를 전달할때 호출
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeString(name);
        dest.writeArray(new Mlos[]{mlos});
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mlos getMlos() {
        return mlos;
    }

    public void setMlos(Mlos mlos) {
        this.mlos = mlos;
    }
}
