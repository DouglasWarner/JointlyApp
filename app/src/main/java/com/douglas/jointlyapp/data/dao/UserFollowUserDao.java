package com.douglas.jointlyapp.data.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.UserFollowUser;

import java.util.List;

/**
 * Interface UserFollowUserDao
 */
@Dao
public interface UserFollowUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserFollowUser userFollowUser);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(List<UserFollowUser> userFollowUser);

    @Update
    void update(UserFollowUser userFollowUser);

    @Delete
    void delete(UserFollowUser userFollowUser);

    @Query("SELECT * FROM userFollowUser")
    List<UserFollowUser> getList();

    @Query("SELECT COUNT(*) FROM userFollowUser where user_follow=:userEmail")
    long getCountUserFollowers(String userEmail);

    @Query("SELECT user_follow FROM userFollowUser where user=:userEmail")
    List<String> getListFollows(String userEmail);

    @Query("SELECT * FROM userFollowUser WHERE user=:userEmail AND user_follow=:userFollowed AND is_deleted=:is_deleted")
    UserFollowUser getUserFollowUser(String userEmail, String userFollowed, boolean is_deleted);

    @Query("DELETE FROM userFollowUser")
    void deleteAll();

    @Query("SELECT * FROM userFollowUser WHERE is_deleted=:isDeleted OR is_sync=:isSync")
    List<UserFollowUser> getListToSync(boolean isDeleted, boolean isSync);

    @Transaction
    default void upsert(UserFollowUser obj) {
        long id = insert(obj);
        if (id == -1) {
            update(obj);
        }
    }

    @Transaction
    default void syncFromAPI(List<UserFollowUser> list) {
        deleteAll();
        List<Long> insertResult = insert(list);

        Log.e("TAG", "Tipo -----------> USER FOLLOW USER <-------------");
        insertResult.forEach(x-> Log.e("TAG", "Sync Insert -------------------> " + x));
    }
}
