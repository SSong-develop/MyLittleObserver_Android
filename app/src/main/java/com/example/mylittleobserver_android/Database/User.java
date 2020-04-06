package com.example.mylittleobserver_android.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String UserName;
    private String DeviceNumber;

    public User() {}

    public User(int id) {
        this.id = id;
    }

    public User(String userName, String deviceNumber) {
        this.UserName = userName;
        this.DeviceNumber = deviceNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDeviceNumber() {
        return DeviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        DeviceNumber = deviceNumber;
    }

    @NonNull
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("id=").append(id);
        sb.append(", UserName='").append(UserName);
        sb.append(", DeviceNumber='").append(DeviceNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

