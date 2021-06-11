package com.douglas.jointlyapp.data.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.UserJoinInitiative;

import java.util.List;

@Dao
public interface UserJoinInitiativeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserJoinInitiative userJoinInitiative);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(List<UserJoinInitiative> obj);

    @Query("INSERT OR IGNORE INTO userJoinInitiative (id_initiative,user_email) VALUES(:id_initiative,:user_email) ")
    long insert(long id_initiative, String user_email);

    @Delete
    void delete(UserJoinInitiative userJoinInitiative);

    @Update
    void update(UserJoinInitiative userJoinInitiative);

    @Query("UPDATE userJoinInitiative SET type=:type, is_deleted=:is_deleted, is_sync=:is_sync WHERE id_initiative=:id_initiative AND user_email=:user_email")
    void update(long id_initiative, String user_email, int type, boolean is_deleted, boolean is_sync);

    @Query("SELECT * FROM userJoinInitiative WHERE is_deleted=:is_deleted")
    List<UserJoinInitiative> getList(boolean is_deleted);

    @Query("SELECT count(*) FROM userJoinInitiative j WHERE j.id_initiative IN (SELECT i.id FROM initiative i) AND j.is_deleted=:is_deleted GROUP BY j.id_initiative ORDER BY j.id_initiative")
    List<Long> getListCountUserJoinedByInitiative(boolean is_deleted);

    @Query("SELECT count(*) FROM userJoinInitiative WHERE user_email=:user AND type=:type AND is_deleted=:is_deleted")
    long getCountInitiativeParticipateByUser(String user, int type, boolean is_deleted);

    @Query("SELECT id_initiative FROM userJoinInitiative WHERE user_email=:userEmail")
    List<Long> getListInitiativeByUserJoined(String userEmail);

    @Query("SELECT * FROM userJoinInitiative WHERE id_initiative=:idInitiative AND user_email=:userEmail AND is_deleted=:is_deleted")
    UserJoinInitiative getUserJoinInitiative(long idInitiative, String userEmail, boolean is_deleted);

    @Query("DELETE FROM userJoinInitiative")
    void deleteAll();

    @Query("SELECT * FROM userJoinInitiative WHERE is_deleted=:isDeleted OR is_sync=:isSync")
    List<UserJoinInitiative> getListToSync(boolean isDeleted, boolean isSync);

    @Transaction
    default void upsert(UserJoinInitiative obj) {
        long id = insert(obj.getId_initiative(), obj.getUser_email());
        if (id == -1) {
            update(obj.getId_initiative(), obj.getUser_email(), obj.getType(), obj.getIs_deleted(), obj.getIs_sync());
        }
    }

    @Transaction
    default void syncFromAPI(List<UserJoinInitiative> list) {
        deleteAll();
        List<Long> insertResult = insert(list);

        Log.e("TAG", "Tipo ------> UserJoin");
        insertResult.forEach(x-> Log.e("TAG", "Sync Insert -------------------> " + x));
    }
}
