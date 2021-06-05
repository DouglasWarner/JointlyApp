package com.douglas.jointlyapp.data.repository;

import android.preference.ListPreference;
import android.util.Log;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.dao.UserDao;
import com.douglas.jointlyapp.data.dao.UserFollowUserDao;
import com.douglas.jointlyapp.data.dao.UserReviewUserDao;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserReviewUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserRepository {

    private static final UserRepository userRepository;
    private final UserDao userDao;
    private final UserFollowUserDao userFollowUserDao;
    private final UserReviewUserDao userReviewUserDao;
    private List<User> list;
    private int countUserFollowers;

    static {
        userRepository = new UserRepository();
    }

    private UserRepository() {
        this.list = new ArrayList<>();
        countUserFollowers = 0;
        JointlyDatabase db = JointlyDatabase.getDatabase();
        userDao = db.userDao();
        userFollowUserDao = db.userFollowUserDao();
        userReviewUserDao = db.UserReviewUserDao();
    }

    public static UserRepository getInstance()
    {
        return userRepository;
    }

    //region User

    /**
     * Devuelve la lista de usuarios
     * @return list
     */
    public List<User> getList() {
        List<User> result;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(userDao::getList).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
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
     * Update or Insert for Sync with the API
     * @param list
     */
    public void upsertUser(List<User> list) {
        Boolean b = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.upsert(list)).isDone();
    }

    /**
     * Valida el login de la aplicacion
     * @param email
     * @param password
     * @return true if the login was successful, false if not
     */
    public boolean validateCredentials(String email, String password) {
        boolean result;
        User user = null;
        try {
            user = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getUser(email, password)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (user == null) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }

    /**
     * Devuelve el usuario para por parametros
     * @param email
     * @return user
     */
    public User getUser(String email) {
        User result;
        User user = null;
        try {
            user = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getUserExists(email)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = user;
        }
        return result;
    }

    /**
     * Devuelve si existe el usuario pasado por parametros
     * @param email
     * @return true if exists, false if not exists
     */
    public boolean userExists(String email) {
        boolean result;
        User user = null;
        try {
            user = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getUserExists(email)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (user == null) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }

    /**
     * Devuelve la lista de usuarios seguidos
     * @param userEmail
     * @return list
     */
    public List<User> getListUserFollowed(String userEmail) {
        List<User> result;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getListUserFollowed(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    /**
     * Devuelve la lista de usuarios unidos a la iniciativa
     * @param idInitiative
     * @return list
     */
    public List<User> getListUserJoined(long idInitiative) {
        List<User> result;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getListUserJoined(idInitiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    /**
     * Devuelve el usuario propietario de la iniciativa
     * @param initiative
     * @return user
     */
    public User getUserOwnerInitiative(int initiative) {
        User result;
        User user = null;

        try {
            user = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getUserOwner(initiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = user;
        }
        return result;
    }

    /**
     * Devuelve la lista de usuarios que han creado almenos una iniciativa
     * @return list
     */
    public List<User> getListInitiativeOwners() {
        List<User> result;

        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(userDao::getListInitiativeOwners).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    //endregion

    //region UserFollowUser

    /**
     * Obtiene todos los datos de la tabla
     */
    public List<UserFollowUser> getListUserFollow() {
        List<UserFollowUser> result = null;

        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(userFollowUserDao::getList).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Obtiene todos los datos eliminados de la tabla
     * @param isDeleted
     * @return list
     */
    public List<UserFollowUser> getListUserFollowDeleted(boolean isDeleted) {
        List<UserFollowUser> result = null;

        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getListDeleted(isDeleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
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
     * Update or Insert for Sync with the API
     * @param list
     */
    public void upsertUserFollowUser(List<UserFollowUser> list) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.upsert(list));
    }

    /**
     * Devuelve la cantidad de usuarios que le siguen
     * @param userEmail
     * @return row count
     */
    public int getCountUserFollowers(String userEmail) {
        int result;
        try {
            countUserFollowers = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getCountUserFollowers(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = countUserFollowers;
        }
        return result;
    }

    /**
     * Devuelve la lista de usuarios seguidores
     * @param userEmail
     * @return list
     */
    public List<String> getListUserFollows(String userEmail) {
        List<String> result;
        List<String> list = null;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getListFollows(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    /**
     * Devuelve el usuario que le sigue al otro
     * @param userEmail
     * @param userFollowed
     * @return user follow
     */
    public UserFollowUser getUserFollowUser(String userEmail, String userFollowed) {
        UserFollowUser result;
        UserFollowUser userFollowUser = null;

        try {
            userFollowUser = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getUserFollowUser(userEmail, userFollowed)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = userFollowUser;
        }
        return result;
    }

    /**
     * Elimina todos los datos de la tabla
     */
    public void deleteAllUserFollow() {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(userFollowUserDao::deleteAll);
    }

    //endregion

    //region UserReviewUser

    /**
     * Inserta un review en un usuario
     * @param userReviewUser
     */
    public void insertUserReview(UserReviewUser userReviewUser)
    {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.insert(userReviewUser));
    }

    /**
     * Actualiza el review
     * @param userReviewUser
     */
    public void updateReview(UserReviewUser userReviewUser)
    {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.update(userReviewUser));
    }

    /**
     * Borra el review de un usuario
     * @param userReviewUser
     */
    public void deleteUserReview(UserReviewUser userReviewUser)
    {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.delete(userReviewUser));
    }

    /**
     * Devuelve la lista de review de un usuario
     * @param userEmail
     * @return list
     */
    public List<UserReviewUser> getListUserReviews(String userEmail) {
        List<UserReviewUser> result;
        List<UserReviewUser> list = null;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.getListReview(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    /**
     * Devuelve la lista de reviews eliminados
     * @param isDeleted
     * @return list
     */
    public List<UserReviewUser> getListUserReviewsDeleted(boolean isDeleted) {
        List<UserReviewUser> result = null;

        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.getListDeleted(isDeleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Update or Insert for Sync with the API
     * @param list
     */
    public void upsertUserReviewUser(List<UserReviewUser> list) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.upsert(list));
    }

    /**
     * Elimina todos los datos de la tabla
     */
    public void deleteAllUserReview() {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(userReviewUserDao::deleteAll);
    }

    //endregion
}
