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
public interface InitiativeDao extends BaseDao<Initiative> {

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

    @Query("SELECT * FROM initiative i ORDER BY (SELECT COUNT(*) FROM userJoinInitiative u WHERE i.id=u.id_initiative)")
    List<Initiative> getListOrderByMaxUserJoined();

    @Query("SELECT * FROM initiative WHERE created_by=:userEmail")
    List<Initiative> getListUserCreated(String userEmail);

    @Query("SELECT * FROM initiative WHERE id IN (SELECT id_initiative FROM userJoinInitiative WHERE user_email=:userEmail)")
    List<Initiative> getListUserJoined(String userEmail);

    @Query("SELECT * FROM initiative WHERE id =:idInitiative")
    Initiative getInitiative(long idInitiative);

    @Query("DELETE FROM initiative")
    void deleteAll();

    @Query("SELECT * FROM initiative WHERE is_deleted=:isDeleted")
    List<Initiative> getListDeleted(boolean isDeleted);
}
