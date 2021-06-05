package com.douglas.jointlyapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.douglas.jointlyapp.data.model.Chat;

import java.util.List;

@Dao
public interface ChatDao extends BaseDao<Chat> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Chat chat);

    @Query("SELECT * FROM chat")
    List<Chat> getList();

    @Query("SELECT * FROM chat WHERE idInitiative=:idInitiative")
    List<Chat> getChatInitiative(long idInitiative);
}
