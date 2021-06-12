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

/**
 * Interface UserReviewUser
 */
@Dao
public interface UserReviewUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserReviewUser userReviewUser);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(List<UserReviewUser> userReviewUser);

    @Update
    void update(UserReviewUser userReviewUser);

    @Delete
    void delete(UserReviewUser userReviewUser);

    @Query("SELECT * FROM userReviewUser WHERE is_deleted=:is_deleted")
    List<UserReviewUser> getList(boolean is_deleted);

    @Query("SELECT * FROM userReviewUser WHERE user_review=:userEmail AND is_deleted=:is_deleted")
    List<UserReviewUser> getListReview(String userEmail, boolean is_deleted);

    @Query("SELECT * FROM userReviewUser WHERE is_deleted=:isDeleted OR is_sync=:isSync")
    List<UserReviewUser> getListToSync(boolean isDeleted, boolean isSync);

    @Query("DELETE FROM userReviewUser")
    void deleteAll();

    @Transaction
    default void syncFromAPI(List<UserReviewUser> list) {
        deleteAll();
        List<Long> insertResult = insert(list);

        Log.e("TAG", "Tipo ----------> USER REVIEW <-------------");
        insertResult.forEach(x-> Log.e("TAG", "Sync Insert -------------------> " + x));
    }
}
