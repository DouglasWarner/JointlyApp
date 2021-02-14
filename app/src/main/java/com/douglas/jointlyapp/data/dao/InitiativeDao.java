package com.douglas.jointlyapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.Initiative;

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
    List<Initiative> get();

    @Query("SELECT COUNT(*) FROM initiative")
    int getRowCount();
}
