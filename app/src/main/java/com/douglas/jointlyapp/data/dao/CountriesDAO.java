package com.douglas.jointlyapp.data.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.douglas.jointlyapp.data.model.Countries;

import java.util.List;

/**
 * Interface CountriesDAO
 */
@Dao
public interface CountriesDAO extends BaseDao<Countries> {

    @Query("SELECT * FROM countries")
    List<Countries> getList();

    @Query("DELETE FROM countries")
    void deleteAll();

    @Transaction
    default void syncFromAPI(List<Countries> list) {
        deleteAll();
        List<Long> insertResult = insert(list);

        Log.e("TAG", "Tipo ------> Countries");
        insertResult.forEach(x-> Log.e("TAG", "Sync Insert -------------------> " + x));
    }
}
