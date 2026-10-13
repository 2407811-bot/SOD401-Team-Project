
package com.example.sod401_team_project.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertUser(User user);

    @Query("SELECT * FROM users ORDER BY full_name ASC")
    List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getUserById(int userId);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Update
    int updateUser(User user);

    @Delete
    int deleteUser(User user);

    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();
}
