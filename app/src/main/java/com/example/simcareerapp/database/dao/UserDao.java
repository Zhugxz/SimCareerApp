package com.example.simcareerapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.simcareerapp.database.entity.UserEntity;

@Dao
public interface UserDao {

    @Insert
    public void insertUser(UserEntity user);

    @Update
    public void updateUser(UserEntity user);

    /**
     * find a user by email
     * @param email
     * @return User entity
     */
    @Query("select * from users where user_email = :email")
    public UserEntity findUserByEmail(String email);

    /**
     * get user by email and password
     * @param email
     * @param password
     * @return user entity
     */
    @Query("select * from users where user_email = :email and user_password = :password")
    public UserEntity findUserByEmailPass(String email, String password);
}
