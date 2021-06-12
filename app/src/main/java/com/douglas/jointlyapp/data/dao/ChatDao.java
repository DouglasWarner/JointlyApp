package com.douglas.jointlyapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.Chat;

import java.util.List;

/**
 * Interface ChaDao
 */
@Dao
public interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Chat chat);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(List<Chat> chat);

    @Update
    void update(Chat obj);

    @Update
    void update(List<Chat> obj);

    @Query("SELECT * FROM chat")
    List<Chat> getList();

    @Query("SELECT * FROM chat WHERE idInitiative=:idInitiative")
    List<Chat> getChatInitiative(long idInitiative);

    @Transaction
    default void upsert(Chat obj) {
        long id = insert(obj);
        if (id == -1) {
            update(obj);
        }
    }
}
