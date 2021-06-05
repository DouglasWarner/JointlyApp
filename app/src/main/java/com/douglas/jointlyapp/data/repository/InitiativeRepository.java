package com.douglas.jointlyapp.data.repository;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.dao.InitiativeDao;
import com.douglas.jointlyapp.data.dao.UserJoinInitiativeDao;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class InitiativeRepository {

    private static final InitiativeRepository initiativeRepository;
    private final InitiativeDao initiativeDao;
    private final UserJoinInitiativeDao userJoinInitiativeDao;
    private List<Initiative> list;

    static {
        initiativeRepository = new InitiativeRepository();
    }

    private InitiativeRepository()
    {
        this.list = new ArrayList<>();
        JointlyDatabase db = JointlyDatabase.getDatabase();
        initiativeDao = db.initiativeDao();
        userJoinInitiativeDao = db.userJoinInitiativeDao();
    }

    public static InitiativeRepository getInstance()
    {
        return initiativeRepository;
    }

    //region Initiative

    /**
     * Devuelve toda la lista de iniciativas
     * @return list
     */
    public List<Initiative> getList() {
        List<Initiative> result;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(initiativeDao::getList).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    /**
     * Devuelve la lista de todas las iniciativas excepto del usuario pasado por parametros
     * @param userEmail
     * @return list
     */
    public List<Initiative> getList(String userEmail) {
        List<Initiative> result;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(initiativeDao::getList).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list.stream().filter(x -> !x.getCreated_by().equals(userEmail)).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * Obtiene todos los datos eliminados de la tabla
     * @param isDeleted
     * @return list
     */
    public List<Initiative> getListInitiativeDeleted(boolean isDeleted) {
        List<Initiative> result = null;

        try {
            JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListDeleted(isDeleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Inserta la iniciativa
     * @param initiative
     * @return id
     */
    public long insert(Initiative initiative) {
        long res;
        long result = 0;

        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.insert(initiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            res = result;
        }
        return res;
    }

    /**
     * Actualiza la iniciativa
     * @param editInitiative
     */
    public void update(Initiative editInitiative) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.update(editInitiative));
    }

    /**
     * Update or Insert for Sync with the API
     * @param list
     */
    public void upsertInitiative(List<Initiative> list) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.upsert(list));
    }

    /**
     * Borra la iniciativa
     * @param initiative
     */
    public void delete(Initiative initiative) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.delete(initiative));
    }

    /**
     * Devuelve la lista de iniciativas creadas por el usuario
     * @param userEmail
     * @return list
     */
    public List<Initiative> getListCreatedByUser(String userEmail) {
        List<Initiative> result;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListUserCreated(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    /**
     * Devuelve la lista de iniciativas a las que esta unido
     * @param userEmail
     * @return list
     */
    public List<Initiative> getListJoinedByUser(String userEmail) {
        List<Initiative> result;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListUserJoined(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    /**
     * Devuelve la lista de iniciativas que ha creado el usuario
     * @param userEmail
     * @return list
     */
    public List<Initiative> getListUserCreated(String userEmail) {
        List<Initiative> result;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListUserCreated(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    /**
     * Devuelve la lista de usuarios unidos a la iniciativa
     * @param userEmail
     * @return list
     */
    public List<Initiative> getListUserJoined(String userEmail) {
        List<Initiative> result;
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListUserJoined(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = list;
        }
        return result;
    }

    /**
     * Devuelve la iniciativa
     * @param idInitiative
     * @return initiative
     */
    public Initiative getInitiative(long idInitiative) {
        Initiative result;
        Initiative initiative = null;
        try {
            initiative = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getInitiative(idInitiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = initiative;
        }
        return result;
    }

    /**
     * Elimina todos los datos de la tabla
     */
    public void deleteAllInitiative() {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(initiativeDao::deleteAll);
    }

    //endregion

    //region UserJoinInitiative

    /**
     * Obtiene todos los datos eliminados de la tabla
     * @param isDeleted
     * @return list
     */
    public List<UserJoinInitiative> getListUsersJoinedDeleted(boolean isDeleted) {
        List<UserJoinInitiative> result = null;

        try {
            JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.getListDeleted(isDeleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Devuelve el usuario unido a la iniciativa
     * @param idInitiative
     * @param userEmail
     * @return user join
     */
    public UserJoinInitiative getUserJoined(long idInitiative, String userEmail) {
        UserJoinInitiative result;
        UserJoinInitiative userJoinInitiative = null;

        try {
            userJoinInitiative = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.getUserJoinInitiative(idInitiative, userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            result = userJoinInitiative;
        }
        return result;
    }

    /**
     * Update or Insert for Sync with the API
     * @param list
     */
    public void upsertUserJoinInitiative(List<UserJoinInitiative> list) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.upsert(list));
    }

    /**
     * Inserta la union de un usuario con la iniciativa
     * @param userJoinInitiative
     */
    public void insertUserJoin(UserJoinInitiative userJoinInitiative) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(()-> userJoinInitiativeDao.insert(userJoinInitiative));
    }

    /**
     * Cancela la union del usuario con la iniciativa
     * @param userJoinInitiative
     */
    public void deleteUserJoin(UserJoinInitiative userJoinInitiative) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(()-> userJoinInitiativeDao.delete(userJoinInitiative));
    }

    /**
     * Elimina todos los datos de la tabla
     */
    public void deleteAllUserJoin() {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(userJoinInitiativeDao::deleteAll);
    }

    //endregion
}
