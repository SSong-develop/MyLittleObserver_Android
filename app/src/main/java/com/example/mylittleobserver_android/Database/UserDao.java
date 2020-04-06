
package com.example.mylittleobserver_android.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    // Data Access Object

    // 위에 있는 Query문을 사용할때에는 밑에 있는 getAll() 메소드를 호출하면 된다.
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}