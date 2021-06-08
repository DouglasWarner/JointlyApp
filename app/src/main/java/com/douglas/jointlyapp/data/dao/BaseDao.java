package com.douglas.jointlyapp.data.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.UserFollowUser;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(T obj);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(List<T> obj);

    @Update
    void update(T obj);

    @Update
    void update(List<T> obj);

    @Delete
    void delete(T obj);

    @Transaction
    default void upsert(T obj) {
        long id = insert(obj);
        if (id == -1) {
            update(obj);
        }
    }

    @Transaction
    default void upsert(List<T> objList) {
        List<Long> insertResult = insert(objList);
        List<T> updateList = new ArrayList<>();

        insertResult.forEach(x-> Log.e("TAG", "Insert -------------------> " + x));

        for (int i = 0; i < insertResult.size(); i++) {
            Log.e("TAG", "Tipo ------> "+objList.get(0).getClass().toString());
            if (insertResult.get(i) == -1) {
                updateList.add(objList.get(i));
            }
        }

        if (!updateList.isEmpty()) {
            update(updateList);
        }
    }
}