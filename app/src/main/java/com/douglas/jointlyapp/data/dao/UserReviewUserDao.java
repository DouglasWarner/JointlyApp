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
public interface UserReviewUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserReviewUser userReviewUser);

    @Update
    void update(UserReviewUser userReviewUser);

    @Delete
    void delete(UserReviewUser userReviewUser);

    @Query("SELECT * FROM userReviewUser where idUserReview=:userEmail")
    List<UserReviewUser> getListReview(String userEmail);
}
