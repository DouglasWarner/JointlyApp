package com.douglas.jointlyapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.UserJoinInitiative;

import java.util.List;

@Dao
public interface UserJoinInitiativeDao extends BaseDao<UserJoinInitiative> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserJoinInitiative userJoinInitiative);

    @Delete
    void delete(UserJoinInitiative userJoinInitiative);

    @Update
    void update(UserJoinInitiative userJoinInitiative);

    @Query("SELECT * FROM userJoinInitiative")
    List<UserJoinInitiative> getList();

    @Query("SELECT * FROM userJoinInitiative WHERE is_deleted=:isDeleted")
    List<UserJoinInitiative> getListDeleted(boolean isDeleted);

    @Query("SELECT COUNT(*) FROM userJoinInitiative")
    int getRowCount();

    @Query("SELECT id_initiative FROM userJoinInitiative WHERE user_email=:userEmail")
    List<Integer> getListInitiativeByUserJoined(String userEmail);

    @Query("SELECT * FROM userJoinInitiative where id_initiative=:idInitiative AND user_email=:userEmail")
    UserJoinInitiative getUserJoinInitiative(long idInitiative, String userEmail);

    @Query("DELETE FROM userJoinInitiative")
    void deleteAll();
}
