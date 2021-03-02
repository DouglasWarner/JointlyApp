package com.douglas.jointlyapp.data.repository;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.BoringLayout;
import android.util.Log;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.dao.UserDao;
import com.douglas.jointlyapp.data.dao.UserFollowUserDao;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserRepository {

    private static UserRepository userRepository;
    private JointlyDatabase db;
    private UserDao userDao;
    private UserFollowUserDao userFollowUserDao;
    private List<User> list;
    private int countUserFollowers;

    static {
        userRepository = new UserRepository();
    }

    private UserRepository() {
        this.list = new ArrayList<>();
        countUserFollowers = 0;
        db = JointlyDatabase.getDatabase();
        userDao = db.userDao();
        userFollowUserDao = db.userFollowUserDao();
    }

    public static UserRepository getInstance()
    {
        return userRepository;
    }

    /**
     * Devuelve la lista de usuarios
     * @return
     */
    public List<User> getList()
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getList()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally
        {
            return list;
        }
    }

    /**
     * Inserta un usuario nuevo
     * @param user
     */
    public void insert(User user)
    {
        Boolean b = null;

        b = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.insert(user)).isDone();

        Log.d("TAG", "Insertado: " + b);
    }

    /**
     * Actualiza el usuario
     * @param user
     */
    public void update(User user)
    {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.update(user));
    }

    /**
     * Inserta un usuario que sigue a otro
     * @param userFollowUser
     */
    public void insertUserFollowed(UserFollowUser userFollowUser)
    {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.insert(userFollowUser));
    }

    /**
     * Borra el seguimiento de un usuario a otro
     * @param userFollowUser
     */
    public void deleteUserFollowed(UserFollowUser userFollowUser)
    {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.delete(userFollowUser));

    }

    /**
     * Devuelve la cantidad de usuarios que le siguen
     * @param userEmail
     * @return
     */
    public int getCountUserFollowers(String userEmail)
    {
        try {
            countUserFollowers = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getCountUserFollowers(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return countUserFollowers;
        }
    }

    /**
     * Valida el login de la aplicacion
     * @param email
     * @param password
     * @return
     */
    public boolean validateCredentials(String email, String password)
    {
        User user = null;
        try {
            user = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getUser(email, password)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally
        {
            if(user == null)
                return false;
            else
                return true;
        }
    }

    /**
     * Devuelve el usuario para por parametros
     * @param email
     * @return
     */
    public User getUser(String email)
    {
        User user = null;
        try {
            user = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getUserExists(email)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally
        {
            return user;
        }
    }

    /**
     * Devuelve si existe el usuario pasado por parametros
     * @param email
     * @return
     */
    public boolean userExists(String email)
    {
        User user = null;
        try {
            user = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getUserExists(email)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally
        {
            if(user == null)
                return false;
            else
                return true;
        }
    }

    /**
     * Devuelve la lista de usuarios seguidores
     * @param userEmail
     * @return
     */
    public List<String> getListUserFollows(String userEmail)
    {
        List<String> list = null;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getListFollows(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    /**
     * Devuelve el usuario que le sigue al otro
     * @param userEmail
     * @param userFollowed
     * @return
     */
    public UserFollowUser getUserFollowUser(String userEmail, String userFollowed)
    {
        UserFollowUser userFollowUser = null;

        try {
            userFollowUser = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getUserFollowUser(userEmail, userFollowed)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return userFollowUser;
        }
    }

    /**
     * Devuelve la lista de usuarios seguidos
     * @param userEmail
     * @return
     */
    public List<User> getListUserFollowed(String userEmail)
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getListUserFollowed(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }finally {
           return list;
        }
    }

    /**
     * Devuelve la lista de usuarios unidos a la iniciativa
     * @param idInitiative
     * @return
     */
    public List<User> getListUserJoined(int idInitiative)
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getListUserJoined(idInitiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    /**
     * Devuelve el usuario propietario de la iniciativa
     * @param initiative
     * @return
     */
    public User getUserOwnerInitiative(int initiative)
    {
        User user = null;

        try {
            user = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(()-> userDao.getUserOwner(initiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            return user;
        }
    }

    /**
     * Devuelve la lista de usuarios que han creado almenos una iniciativa
     * @return
     */
    public List<User> getListInitiativeOwners() {

        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getListInitiativeOwners()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }
}
