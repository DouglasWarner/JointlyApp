package com.douglas.jointlyapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.douglas.jointlyapp.data.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM user")
    List<User> getList();

    @Query("SELECT COUNT(*) FROM user")
    int getRowCount();

    @Query("SELECT * FROM user WHERE email=:userEmail AND password=:userPassword")
    User getUser(String userEmail, String userPassword);

    @Query("SELECT * FROM user WHERE email=:userEmail")
    User getUserExists(String userEmail);

    @Query("SELECT * FROM user ORDER BY location")
    List<User> getListOrderByLocation();

    @Query("SELECT * FROM user u ORDER BY (SELECT COUNT(*) FROM userFollowUser f WHERE u.email=f.idUser)")
    List<User> getListOrderByMaxUserFollowers();

    @Query("SELECT * FROM user WHERE email IN (SELECT idUserFollowed FROM userFollowUser WHERE idUser=:userEmail)")
    List<User> getListUserFollowed(String userEmail);

    @Query("SELECT * FROM user WHERE email IN (SELECT idUser FROM userJoinInitiative WHERE idInitiative=:idInitiative)")
    List<User> getListUserJoined(int idInitiative);

    @Query("SELECT * FROM user WHERE email in (SELECT createdBy FROM initiative WHERE id=:idInitiative)")
    User getUserOwner(int idInitiative);

    @Query("SELECT * FROM user WHERE email in (SELECT createdBy FROM initiative)")
    List<User> getListInitiativeOwners();
}
