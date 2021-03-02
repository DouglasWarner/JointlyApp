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
public interface UserJoinInitiativeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserJoinInitiative userJoinInitiative);

    @Delete
    void delete(UserJoinInitiative userJoinInitiative);

    @Update
    void update(UserJoinInitiative userJoinInitiative);

    @Query("SELECT * FROM userJoinInitiative")
    List<UserJoinInitiative> getList();

    @Query("SELECT COUNT(*) FROM userJoinInitiative")
    int getRowCount();

    @Query("SELECT idInitiative FROM userJoinInitiative WHERE idUser=:userEmail")
    List<Integer> getListInitiativeByUserJoined(String userEmail);

    @Query("SELECT * FROM userJoinInitiative where idInitiative=:idInitiative AND idUser=:userEmail")
    UserJoinInitiative getUserJoinInitiative(int idInitiative, String userEmail);
}
