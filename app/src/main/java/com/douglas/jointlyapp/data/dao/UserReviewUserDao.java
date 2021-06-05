package com.douglas.jointlyapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
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

    @Query("SELECT * FROM userReviewUser where user_review=:userEmail")
    List<UserReviewUser> getListReview(String userEmail);

    @Query("SELECT * FROM userReviewUser WHERE is_deleted=:isDeleted")
    List<UserReviewUser> getListDeleted(boolean isDeleted);

    @Query("DELETE FROM userReviewUser")
    void deleteAll();
}
