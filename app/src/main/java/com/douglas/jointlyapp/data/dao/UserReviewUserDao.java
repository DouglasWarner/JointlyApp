package com.douglas.jointlyapp.data.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.UserReviewUser;

import java.util.List;

@Dao
public interface UserReviewUserDao extends BaseDao<UserReviewUser> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserReviewUser userReviewUser);

    @Update
    void update(UserReviewUser userReviewUser);

    @Delete
    void delete(UserReviewUser userReviewUser);

    @Query("SELECT * FROM userReviewUser")
    List<UserReviewUser> getList();

    @Query("SELECT * FROM userReviewUser where user_review=:userEmail")
    List<UserReviewUser> getListReview(String userEmail);

    @Query("SELECT * FROM userReviewUser WHERE is_deleted=:isDeleted OR is_sync=:isSync")
    List<UserReviewUser> getListToSync(boolean isDeleted, boolean isSync);

    @Query("DELETE FROM userReviewUser")
    void deleteAll();

    @Transaction
    default void syncFromAPI(List<UserReviewUser> list) {
        deleteAll();
        List<Long> insertResult = insert(list);

        Log.e("TAG", "Tipo ------> UserReview");
        insertResult.forEach(x-> Log.e("TAG", "Sync Insert -------------------> " + x));
    }
}
