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
public interface UserDao extends BaseDao<User> {

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

    @Query("SELECT * FROM user u ORDER BY (SELECT COUNT(*) FROM userFollowUser f WHERE u.email=f.user)")
    List<User> getListOrderByMaxUserFollowers();

    @Query("SELECT * FROM user WHERE email IN (SELECT user_follow FROM userFollowUser WHERE user=:userEmail)")
    List<User> getListUserFollowed(String userEmail);

    @Query("SELECT * FROM user WHERE email IN (SELECT user_email FROM userJoinInitiative WHERE id_initiative=:idInitiative)")
    List<User> getListUserJoined(long idInitiative);

    @Query("SELECT * FROM user WHERE email in (SELECT created_by FROM initiative WHERE id=:idInitiative)")
    User getUserOwner(int idInitiative);

    @Query("SELECT * FROM user WHERE email in (SELECT created_by FROM initiative)")
    List<User> getListInitiativeOwners();

    @Query("DELETE FROM user")
    void deleteAll();
}
