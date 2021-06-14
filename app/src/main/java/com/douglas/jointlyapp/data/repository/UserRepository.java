package com.douglas.jointlyapp.data.repository;

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

/**
 * Entity that connect to SQlite
 */
public class UserRepository {

    private static final UserRepository userRepository;
    private final UserDao userDao;
    private final UserFollowUserDao userFollowUserDao;
    private final UserReviewUserDao userReviewUserDao;

    static {
        userRepository = new UserRepository();
    }

    private UserRepository() {
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
        List<User> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(userDao::getList).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Inserta un usuario nuevo
     * @param user
     */
    public void insert(User user) {
        Boolean b = null;

        b = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.insert(user)).isDone();

        Log.d("TAG", "Insertado: " + b);
    }

    /**
     * Inserta una lista de usuario
     * @param user
     */
    public List<Long> insert(List<User> user) {
        List<Long> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.insert(user)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Actualiza el usuario
     * @param user
     */
    public void update(User user) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.update(user));
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
        User result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getUserExists(email)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
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
    public List<User> getListUserFollowed(String userEmail, boolean is_deleted) {
        List<User> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getListUserFollowed(userEmail, is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Devuelve la lista de usuarios unidos a la iniciativa
     * @param idInitiative
     * @return list
     */
    public List<User> getListUserJoined(long idInitiative, boolean is_deleted) {
        List<User> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getListUserJoined(idInitiative, is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Devuelve el usuario propietario de la iniciativa
     * @param initiative
     * @return user
     */
    public User getUserOwnerInitiative(int initiative) {
        User result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getUserOwner(initiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Devuelve la lista de usuarios que han creado almenos una iniciativa
     * @return list
     */
    public List<User> getListInitiativeOwners(boolean is_deleted) {
        List<User> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getListInitiativeOwners(is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *  deleteAllUser
     */
    public void deleteAllUser() {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(userDao::deleteAll);
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
    public List<UserFollowUser> getListUserFollowToSync(boolean isDeleted, boolean isSync) {
        List<UserFollowUser> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getListToSync(isDeleted, isSync)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Inserta un usuario que sigue a otro
     * @param userFollowUser
     */
    public void insertUserFollowed(UserFollowUser userFollowUser) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.insert(userFollowUser));
    }

    /**
     * Inserta una lista de usuarios que sigue a otro
     * @param userFollowUser
     */
    public List<Long> insertUserFollowed(List<UserFollowUser> userFollowUser) {
        List<Long> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.insert(userFollowUser)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Update el seguimiento de un usuario a otro
     * @param userFollowUser
     */
    public void updateUserFollowed(UserFollowUser userFollowUser) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.update(userFollowUser));
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
     * Update or Insert for the current userFollowUser
     * @param userFollowUser
     */
    public void upsertUserFollowUser(UserFollowUser userFollowUser) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.upsert(userFollowUser));
    }

    /**
     * Devuelve la cantidad de usuarios que le siguen
     * @param userEmail
     * @return row count
     */
    public long getCountUserFollowers(String userEmail) {
        long result = 0;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getCountUserFollowers(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Devuelve la lista de usuarios seguidores
     * @param userEmail
     * @return list
     */
    public List<String> getListUserFollows(String userEmail) {
        List<String> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getListFollows(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Devuelve el usuario que le sigue al otro
     * @param userEmail
     * @param userFollowed
     * @return user follow
     */
    public UserFollowUser getUserFollowUser(String userEmail, String userFollowed, boolean is_deleted) {
        UserFollowUser result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.getUserFollowUser(userEmail, userFollowed, is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
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
     * Obtiene todos los datos de la tabla
     * @return
     */
    public List<UserReviewUser> getListUserReview(boolean is_deleted) {
        List<UserReviewUser> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.getList(is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Inserta un review en un usuario
     * @param userReviewUser
     */
    public long insertUserReview(UserReviewUser userReviewUser) {
        long result = 0;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.insert(userReviewUser)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Inserta una lista de review en un usuario
     * @param userReviewUser
     */
    public List<Long>  insertUserReview(List<UserReviewUser> userReviewUser) {
        List<Long> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.insert(userReviewUser)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Actualiza el review
     * @param userReviewUser
     */
    public void updateReview(UserReviewUser userReviewUser) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.update(userReviewUser));
    }

    /**
     * Borra el review de un usuario
     * @param userReviewUser
     */
    public void deleteUserReview(UserReviewUser userReviewUser, boolean is_deleted) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.delete(userReviewUser));
    }

    /**
     * Devuelve la lista de review de un usuario
     * @param userEmail
     * @return list
     */
    public List<UserReviewUser> getListUserReviews(String userEmail, boolean is_deleted) {
        List<UserReviewUser> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.getListReview(userEmail, is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Devuelve la lista de reviews eliminados
     * @param isDeleted
     * @return list
     */
    public List<UserReviewUser> getListUserReviewsToSync(boolean isDeleted, boolean isSync) {
        List<UserReviewUser> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.getListToSync(isDeleted, isSync)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Elimina todos los datos de la tabla
     */
    public void deleteAllUserReview() {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(userReviewUserDao::deleteAll);
    }

    /**
     * getListUserToSync
     * @param isSync
     * @return
     */
    public List<User> getListUserToSync(boolean isSync) {
        List<User> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.getListToSync(isSync)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    //endregion

    //region SyncFromAPI

    /**
     * Update or Insert for Sync UserReview with the API
     * @param list
     */
    public void syncUserReviewFromAPI(List<UserReviewUser> list) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userReviewUserDao.syncFromAPI(list));
    }

    /**
     * Update or Insert for Sync UserFollow with the API
     * @param list
     */
    public void syncUserFollowFromAPI(List<UserFollowUser> list) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userFollowUserDao.syncFromAPI(list));
    }

    /**
     * Update or Insert for Sync User with the API
     * @param list
     */
    public void syncUserFromAPI(List<User> list) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userDao.syncFromAPI(list));
    }

    //endregion
}
