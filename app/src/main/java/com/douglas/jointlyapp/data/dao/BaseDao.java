package com.douglas.jointlyapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

/**
 * Interface BaseDao
 * @param <T>
 */
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
}