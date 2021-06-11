package com.douglas.jointlyapp.data.repository;

import android.util.Log;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.dao.InitiativeDao;
import com.douglas.jointlyapp.data.dao.UserJoinInitiativeDao;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Entity that makes the connection to DB local for get data
 */
public class InitiativeRepository {

    private static final InitiativeRepository initiativeRepository;
    private final InitiativeDao initiativeDao;
    private final UserJoinInitiativeDao userJoinInitiativeDao;

    static {
        initiativeRepository = new InitiativeRepository();
    }

    private InitiativeRepository() {
        JointlyDatabase db = JointlyDatabase.getDatabase();
        initiativeDao = db.initiativeDao();
        userJoinInitiativeDao = db.userJoinInitiativeDao();
    }

    public static InitiativeRepository getInstance() {
        return initiativeRepository;
    }

    //region Initiative

    /**
     * Devuelve toda la lista de iniciativas
     * @return list
     */
    public List<Initiative> getList(boolean is_deleted) {
        List<Initiative> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getList(is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Devuelve la lista de todas las iniciativas excepto del usuario pasado por parametros
     * @param userEmail
     * @return list
     */
    public List<Initiative> getList(String userEmail, boolean is_deleted) {
        List<Initiative> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getList(userEmail, is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
//        finally {
//            result = list.stream().filter(x -> !x.getCreated_by().equals(userEmail)).collect(Collectors.toList());
//        }
        return result;
    }

    /**
     * Obtiene todos los datos eliminados de la tabla
     * @param isDeleted
     * @return list
     */
    public List<Initiative> getListInitiativeToSync(boolean isDeleted, boolean isSync) {
        List<Initiative> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListToSync(isDeleted, isSync)).get();
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
        long result = -1;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.insert(initiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Inserta una lista de iniciativa
     * @param initiatives
     * @return id
     */
    public List<Long> insert(List<Initiative> initiatives) {
        List<Long> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.insert(initiatives)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
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
        boolean b = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.upsert(list)).isDone();

        Log.e("TAG", "Initiative from api ----> " + b);
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
    public List<Initiative> getListCreatedByUser(String userEmail, boolean is_deleted) {
        List<Initiative> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListCreatedByUser(userEmail, is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Devuelve la lista de iniciativas según la participación tipo
     * @param userEmail
     * @param type
     * @param is_deleted
     * @return list
     */
    public List<Initiative> getListJoinedByUser(String userEmail, int type, boolean is_deleted) {
        List<Initiative> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getListJoinedByUser(userEmail, type, is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Devuelve la iniciativa
     * @param idInitiative
     * @return initiative
     */
    public Initiative getInitiative(long idInitiative, boolean is_deleted) {
        Initiative result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.getInitiative(idInitiative, is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
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
     * Obtiene todos los datos de la tabla
     * @return
     */
    public List<UserJoinInitiative> getListUserJoinInitiative(boolean is_deleted) {
        try {
            List<UserJoinInitiative> listJoin = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.getList(is_deleted)).get();
            return listJoin;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una lista con todos los usuarios unidos a cada iniciativa
     * @return
     */
    public List<Long> getListCountUsersJoinedByInitiative(boolean is_deleted) {
        List<Long> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.getListCountUserJoinedByInitiative(is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Obtiene el total de iniciativas que realmente a asistido
     * @param user
     * @return count
     */
    public long getCountInitiativeParticipateByUser(String user, int type, boolean is_deleted) {
        long result = 0;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.getCountInitiativeParticipateByUser(user, type, is_deleted)).get();
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
    public List<UserJoinInitiative> getListUsersJoinedToSync(boolean isDeleted, boolean isSync) {
        List<UserJoinInitiative> result = null;

        try {
            JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.getListToSync(isDeleted, isSync)).get();
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
    public UserJoinInitiative getUserJoined(long idInitiative, String userEmail, boolean is_deleted) {
        UserJoinInitiative result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.getUserJoinInitiative(idInitiative, userEmail, is_deleted)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateUserJoin(UserJoinInitiative userJoinInitiative) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.update(userJoinInitiative));
    }

    public void tmpInsert() {
        Initiative i = new Initiative(1,"name","10/10/2021","10/12/2021","description","area","location", CommonUtils.getImagenInitiativeDefault(JointlyApplication.getContext()),"amount","status",
                "douglas@gmail.com",false,false);
        i.setRef_code("123456");
        UserJoinInitiative j = new UserJoinInitiative(1, "douglas@gmail.com", false, false);
        try {
            if(UserRepository.getInstance().tmpInsert() != -1){
                long l = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.insert(i)).get();
                if(l != -1) {
                    JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(()-> userJoinInitiativeDao.insert(j));
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param userJoinInitiative
     */
    public void upsertUserJoin(UserJoinInitiative userJoinInitiative) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.upsert(userJoinInitiative));
    }

    /**
     * Inserta la union de un usuario con la iniciativa
     * @param userJoinInitiative
     */
    public void insertUserJoin(UserJoinInitiative userJoinInitiative) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(()-> userJoinInitiativeDao.insert(userJoinInitiative));
    }

    /**
     * Inserta una lista de union de un usuario con la iniciativa
     * @param userJoinInitiative
     */
    public List<Long> insertUserJoin(List<UserJoinInitiative> userJoinInitiative) {
        List<Long> result = null;
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(()-> userJoinInitiativeDao.insert(userJoinInitiative)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
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

    //region SyncFromAPI

    public void syncUserJoinFromAPI(List<UserJoinInitiative> list) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> userJoinInitiativeDao.syncFromAPI(list));
    }

    public void syncInitiativeFromAPI(List<Initiative> list) {
        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> initiativeDao.syncFromAPI(list));
    }

    //endregion
}
