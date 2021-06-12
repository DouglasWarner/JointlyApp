package com.douglas.jointlyapp.data.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface UserDao
 */
@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(List<User> user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

    @Update
    void update(List<User> user);

    @Query("SELECT * FROM user")
    List<User> getList();

    @Query("SELECT * FROM user WHERE email=:userEmail AND password=:userPassword")
    User getUser(String userEmail, String userPassword);

    @Query("SELECT * FROM user WHERE email=:userEmail")
    User getUserExists(String userEmail);

    @Query("SELECT * FROM user WHERE email IN (SELECT user_follow FROM userFollowUser WHERE user=:userEmail AND is_deleted=:is_deleted)")
    List<User> getListUserFollowed(String userEmail, boolean is_deleted);

    @Query("SELECT * FROM user WHERE email IN (SELECT user_email FROM userJoinInitiative WHERE id_initiative=:idInitiative AND is_deleted=:is_deleted)")
    List<User> getListUserJoined(long idInitiative, boolean is_deleted);

    @Query("SELECT * FROM user WHERE email in (SELECT created_by FROM initiative WHERE id=:idInitiative)")
    User getUserOwner(int idInitiative);

    @Query("SELECT * FROM user WHERE email in (SELECT created_by FROM initiative WHERE is_deleted=:is_deleted)")
    List<User> getListInitiativeOwners(boolean is_deleted);

    @Query("DELETE FROM user")
    void deleteAll();

    @Transaction
    default void syncFromAPI(List<User> list) {
        List<Long> insertResult = insert(list);
        List<User> updateList = new ArrayList<>();

        Log.e("TAG", "Tipo -----------> USER <-------------");

        for (int i = 0; i < insertResult.size(); i++) {
            Log.e("TAG", "Insert -------------------> " + insertResult.get(i));
            if (insertResult.get(i) == -1) {
                updateList.add(list.get(i));
            }
        }

        if (!updateList.isEmpty()) {
            update(updateList);
        }
    }
}
