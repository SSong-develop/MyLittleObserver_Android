package com.example.mylittleobserver_android.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1) // 데이터베이스 객체 선언
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao(); // 이 데이터베이스가 제공하는 interface
    // 이걸 이용해서 User를 이용
}

