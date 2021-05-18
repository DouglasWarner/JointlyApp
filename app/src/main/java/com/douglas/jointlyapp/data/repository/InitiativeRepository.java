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

    private static InitiativeRepository initiativeRepository;
    private JointlyDatabase db;
    private InitiativeDao initiativeDao;
    private UserJoinInitiativeDao userJoinInitiativeDao;
    private List<Initiative> list;

    static {
        initiativeRepository = new InitiativeRepository();
    }

    private InitiativeRepository()
    {
        this.list = new ArrayList<>();
        db = JointlyDatabase.getDatabase();
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
     * @return
     */
    public List<Initiative> getList()
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getList()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    /**
     * Devuelve la lista de todas las iniciativas excepto del usuario pasado por parametros
     * @param userEmail
     * @return
     */
    public List<Initiative> getList(String userEmail)
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getList()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list.stream().filter(x -> !x.getCreatedBy().equals(userEmail)).collect(Collectors.toList());
        }
    }

    /**
     * Inserta la iniciativa
     * @param initiative
     * @return
     */
    public long insert(Initiative initiative)
    {
        long result = 0;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.insert(initiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally
        {
            return result;
        }
    }

    /**
     * Actualiza la iniciativa
     * @param editInitiative
     */
    public void update(Initiative editInitiative) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.update(editInitiative));
    }

    /**
     * Borra la iniciativa
     * @param initiative
     */
    public void delete(Initiative initiative) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.delete(initiative));
    }

    /**
     * Devuelve la lista de iniciativas creadas en curso por el usuario
     * @param userEmail
     * @return
     */
    public List<Initiative> getListCreatedInProgressByUser(String userEmail)
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListUserCreated(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    /**
     * Devulve la lista de iniciativas pasadas creadas por el usuario
     * @param userEmail
     * @return
     */
    public List<Initiative> getListCreatedHistoryByUser(String userEmail)
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListUserCreated(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    /**
     * Devuelve la lista de iniciativas a las que esta unido en curso
     * @param userEmail
     * @return
     */
    public List<Initiative> getListJoinedInProgressByUser(String userEmail)
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListUserJoined(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    /**
     * Devuelve la lista de iniciativas pasadas a las que se unio el usuario
     * @param userEmail
     * @return
     */
    public List<Initiative> getListJoinedHistoryByUser(String userEmail)
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListUserJoined(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    /**
     * Devuelve la lista de iniciativas que ha creado el usuario
     * @param userEmail
     * @return
     */
    public List<Initiative> getListUserCreated(String userEmail)
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListUserCreated(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    /**
     * Devuelve la lista de usuarios unidos a la iniciativa
     * @param userEmail
     * @return
     */
    public List<Initiative> getListUserJoined(String userEmail)
    {
        try {
            list = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListUserJoined(userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    /**
     * Devuelve la iniciativa
     * @param idInitiative
     * @return
     */
    public Initiative getInitiative(int idInitiative) {
        Initiative initiative = null;
        try {
            initiative = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getInitiative(idInitiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            return initiative;
        }
    }

    //endregion

    //region UserJoinInitiative

    /**
     * Devuelve el usuario unido a la iniciativa
     * @param idInitiative
     * @param userEmail
     * @return
     */
    public UserJoinInitiative getUserJoined(int idInitiative, String userEmail)
    {
        UserJoinInitiative userJoinInitiative = null;

        try {
            userJoinInitiative = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.getUserJoinInitiative(idInitiative, userEmail)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            return userJoinInitiative;
        }
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

    //endregion
}
