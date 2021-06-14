package com.douglas.jointlyapp.data.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.douglas.jointlyapp.data.model.TargetArea;

import java.util.List;

/**
 * Interface TargetAreaDao
 */
@Dao
public interface TargetAreaDAO extends BaseDao<TargetArea> {

    @Query("SELECT * FROM targetArea")
    List<TargetArea> getList();

    @Query("DELETE FROM targetArea")
    void deleteAll();

    @Transaction
    default void syncFromAPI(List<TargetArea> list) {
        deleteAll();
        List<Long> insertResult = insert(list);

        Log.e("TAG", "---------------------> TargetArea <-----------------------");
        insertResult.forEach(x-> Log.e("TAG", "----------------------------> Sync Insert -------------------> " + x));
    }
}
