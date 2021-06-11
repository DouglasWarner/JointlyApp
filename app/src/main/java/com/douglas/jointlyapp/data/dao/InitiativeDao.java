package com.douglas.jointlyapp.data.dao;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.Initiative;

import java.util.List;

@Dao
public interface InitiativeDao extends BaseDao<Initiative> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Initiative initiative);

    @Query("INSERT OR IGNORE INTO initiative (id,name,target_date,description,target_area,location," +
            "imagen,target_amount,created_by,ref_code) VALUES(:id,:name,:target_date,:description,:target_area,:location," +
            ":imagen,:target_amount,:created_by,:ref_code) ")
    long insert(long id, String name, String target_date, String description, String target_area, String location,
                Bitmap imagen, String target_amount, String created_by, String ref_code);

    @Delete
    void delete(Initiative initiative);

    @Update
    void update(Initiative initiative);

    @Query("UPDATE initiative SET name=:name, target_date=:target_date, description=:description, location=:location," +
            " imagen=:imagen, target_amount=:target_amount, is_deleted=:is_deleted, is_sync=:is_sync WHERE id=:id")
    void update(long id, String name, String target_date, String description, String location,
                Bitmap imagen, String target_amount, boolean is_deleted, boolean is_sync);

    @Query("SELECT * FROM initiative WHERE is_deleted=:is_deleted")
    List<Initiative> getList(boolean is_deleted);

    @Query("SELECT * FROM initiative WHERE created_by!=:user AND is_deleted=:is_deleted")
    List<Initiative> getList(String user, boolean is_deleted);

    @Query("SELECT * FROM initiative WHERE created_by=:userEmail AND is_deleted=:is_deleted")
    List<Initiative> getListCreatedByUser(String userEmail, boolean is_deleted);

    @Query("SELECT * FROM initiative i WHERE i.id IN (SELECT id_initiative FROM userJoinInitiative WHERE user_email=:userEmail AND type=:type AND is_deleted=:is_deleted);")
    List<Initiative> getListJoinedByUser(String userEmail, int type, boolean is_deleted);

    @Query("SELECT * FROM initiative WHERE id=:idInitiative AND is_deleted=:is_deleted")
    Initiative getInitiative(long idInitiative, boolean is_deleted);

    @Query("DELETE FROM initiative")
    void deleteAll();

    @Query("SELECT * FROM initiative WHERE is_deleted=:isDeleted OR is_sync=:isSync")
    List<Initiative> getListToSync(boolean isDeleted, boolean isSync);

    @Transaction
    default void upsert(Initiative obj) {
        long id = insert(obj.getId(),obj.getName(),obj.getTarget_date(),obj.getDescription(), obj.getTarget_area() ,obj.getLocation(),
                obj.getImagen(),obj.getTarget_amount(), obj.getCreated_by(), obj.getRef_code());
        if (id == -1) {
            update(obj.getId(),obj.getName(),obj.getTarget_date(),obj.getDescription(),obj.getLocation(),
                    obj.getImagen(),obj.getTarget_amount(), obj.getIs_deleted(), obj.getIs_sync());
        }
    }

    @Transaction
    default void syncFromAPI(List<Initiative> list) {
        deleteAll();
        List<Long> insertResult = insert(list);

        Log.e("TAG", "Tipo ------> Initiative");
        insertResult.forEach(x-> Log.e("TAG", "Sync Insert -------------------> " + x));
    }
}
