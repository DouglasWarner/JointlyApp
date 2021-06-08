package com.douglas.jointlyapp.data.repository;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.dao.CountriesDAO;
import com.douglas.jointlyapp.data.dao.TargetAreaDAO;
import com.douglas.jointlyapp.data.model.Countries;
import com.douglas.jointlyapp.data.model.TargetArea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SettingsRepository {

    private static final SettingsRepository respository;
    private final TargetAreaDAO targetAreaDAO;
    private final CountriesDAO countriesDAO;
    private List<TargetArea> targetAreaList;
    private List<Countries> countriesList;

    static {
        respository = new SettingsRepository();
    }

    public SettingsRepository() {
        this.targetAreaList = new ArrayList<>();
        this.countriesList = new ArrayList<>();
        JointlyDatabase db = JointlyDatabase.getDatabase();
        targetAreaDAO = db.targetAreaDao();
        countriesDAO = db.countriesDao();
    }

    public static SettingsRepository getInstance() { return respository; }

    //region TargetArea

    /**
     * Insert all targetArea from API
     * @param targetAreas
     * @return
     */
    public List<Long> insertTargetArea(List<TargetArea> targetAreas) {
        List<Long> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> targetAreaDAO.insert(targetAreas)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get all data from DB
     * @return
     */
    public List<TargetArea> getListTargetArea() {
        try {
            targetAreaList = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(targetAreaDAO::getList).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return targetAreaList;
    }

    /**
     * Synchronized from API
     * @param list
     */
    public void syncTargetAreaFromAPI(List<TargetArea> list) {
        targetAreaList.clear();
        targetAreaList.addAll(list);

        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> targetAreaDAO.syncFromAPI(list));
    }

    //endregion

    //region Countries

    /**
     * Insert all targetArea from API
     * @param countries
     * @return
     */
    public List<Long> insertCountries(List<Countries> countries) {
        List<Long> result = new ArrayList<>();
        try {
            result = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> countriesDAO.insert(countries)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get all data from DB
     * @return
     */
    public List<Countries> getListCountries() {
        try {
            countriesList = JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(countriesDAO::getList).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return countriesList;
    }

    /**
     * Synchronized from API
     * @param list
     */
    public void syncCountriesFromAPI(List<Countries> list) {
        countriesList.clear();
        countriesList.addAll(list);

        JointlyDatabase.DATABASE_WRITE_EXECUTOR.submit(() -> countriesDAO.syncFromAPI(list));
    }

    //endregion
}
