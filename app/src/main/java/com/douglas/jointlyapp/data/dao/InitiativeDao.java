package com.douglas.jointlyapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;

import java.util.List;

@Dao
public interface InitiativeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Initiative initiative);

    @Delete
    void delete(Initiative initiative);

    @Update
    void update(Initiative initiative);

    @Query("SELECT * FROM initiative")
    List<Initiative> getList();

    @Query("SELECT COUNT(*) FROM initiative")
    int getRowCount();

    @Query("SELECT * FROM initiative ORDER BY location")
    List<Initiative> getListOrderByLocation();

    @Query("SELECT * FROM initiative i ORDER BY (SELECT COUNT(*) FROM userJoinInitiative u WHERE i.id=u.idInitiative)")
    List<Initiative> getListOrderByMaxUserJoined();

    @Query("SELECT * FROM initiative WHERE createdBy=:userEmail")
    List<Initiative> getListUserCreated(String userEmail);

    @Query("SELECT * FROM initiative WHERE id IN (SELECT idInitiative FROM userJoinInitiative WHERE idUser=:userEmail)")
    List<Initiative> getListUserJoined(String userEmail);

    @Query("SELECT * FROM initiative WHERE id =:idInitiative")
    Initiative getInitiative(int idInitiative);
}
