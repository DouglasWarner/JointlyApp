package com.douglas.jointlyapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.UserFollowUser;

import java.util.List;

@Dao
public interface UserFollowUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserFollowUser userFollowUser);

    @Update
    void update(UserFollowUser userFollowUser);

    @Delete
    void delete(UserFollowUser userFollowUser);

    @Query("SELECT * FROM userFollowUser")
    List<UserFollowUser> getList();

    @Query("SELECT COUNT(*) FROM userFollowUser where idUserFollowed=:userEmail")
    int getCountUserFollowers(String userEmail);

    @Query("SELECT idUserFollowed FROM userFollowUser where idUser=:userEmail")
    List<String> getListFollows(String userEmail);

    @Query("SELECT * FROM userFollowUser where idUser=:userEmail AND idUserFollowed=:userFollowed")
    UserFollowUser getUserFollowUser(String userEmail, String userFollowed);
}
