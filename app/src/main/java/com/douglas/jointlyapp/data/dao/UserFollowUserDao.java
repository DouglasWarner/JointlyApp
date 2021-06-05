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
public interface UserFollowUserDao extends BaseDao<UserFollowUser> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserFollowUser userFollowUser);

    @Update
    void update(UserFollowUser userFollowUser);

    @Delete
    void delete(UserFollowUser userFollowUser);

    @Query("SELECT * FROM userFollowUser")
    List<UserFollowUser> getList();

    @Query("SELECT * FROM userFollowUser WHERE is_deleted=:isDeleted")
    List<UserFollowUser> getListDeleted(boolean isDeleted);

    @Query("SELECT COUNT(*) FROM userFollowUser where user_follow=:userEmail")
    int getCountUserFollowers(String userEmail);

    @Query("SELECT user_follow FROM userFollowUser where user=:userEmail")
    List<String> getListFollows(String userEmail);

    @Query("SELECT * FROM userFollowUser where user=:userEmail AND user_follow=:userFollowed")
    UserFollowUser getUserFollowUser(String userEmail, String userFollowed);

    @Query("DELETE FROM userFollowUser")
    void deleteAll();
}
