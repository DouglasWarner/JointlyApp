package com.douglas.jointlyapp.ui.initiative.manage;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.douglas.jointlyapp.data.Converters;
import com.douglas.jointlyapp.data.model.Countries;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.TargetArea;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.SettingsRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.services.InitiativeService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageInitiativeInteractorImpl {

    private static final String TAG = "ManageInitiativeInteractorImpl";

    interface ManageInitiativeInteractor {
        void onNameEmpty();
        void onNameFormatError();
        void onLocationEmpty();
        void onTargetAreaEmpty();
        void onTargetDateEmpty();
        void onTargetDateBeforeNowError();
        void onTargetTimeEmpty();
        void onTargetAmountEmpty();
        void onTargetAmountFormatError();

        void onSuccess(Initiative initiative);
        void onUnsuccess();

        void setCountries(List<Countries> countries);
        void setTargetArea(List<TargetArea> targetArea);

        void onError(String message);
    }

    private final ManageInitiativeInteractor interactor;
    private final InitiativeService initiativeService;

    public ManageInitiativeInteractorImpl(ManageInitiativeInteractor manageInitiativeInteractor) {
        this.interactor = manageInitiativeInteractor;
        this.initiativeService = Apis.getInstance().getInitiativeService();
    }

    //region addInitiative

    /**
     *
     * @param name
     * @param targetDate
     * @param targetTime
     * @param description
     * @param targetArea
     * @param location
     * @param imagen
     * @param targetAmount
     * @param created_by
     */
    public void addInitiative(final String name, final String targetDate, final String targetTime,
                              final String description, final String targetArea, final String location, final Bitmap imagen,
                              final String targetAmount, final String created_by) {
        if (isNotValidInitiative(name, targetDate, targetTime, targetArea, location, targetAmount))
            return;

        Initiative initiative = new Initiative(name, CommonUtils.getDateNow(), String.format("%s %s", targetDate, targetTime), description, targetArea,
                location, imagen, targetAmount, created_by);
        initiative.setRef_code(String.valueOf(initiative.hashCode()));

        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            insertToAPI(initiative);
        } else {
            insertToLocal(initiative);
        }
    }
    /**
     *
     * @param addInitiative
     */
    private void insertToLocal(Initiative addInitiative) {
        int result = (int) InitiativeRepository.getInstance().insert(addInitiative);
        Initiative initiative = InitiativeRepository.getInstance().getInitiative(result, false);

        if(initiative != null) {
            interactor.onSuccess(initiative);
        } else {
            interactor.onUnsuccess();
        }
    }

    /**
     *
     * @param initiative
     */
    private void insertToAPI(Initiative initiative) {
        Call<APIResponse<Initiative>> initiativeCall =
                initiativeService.postInitiative(initiative.getName(), CommonUtils.getDateNow(), initiative.getTarget_date(),
                        initiative.getDescription(), initiative.getTarget_area(), initiative.getLocation(), new Converters().fromBitmap(initiative.getImagen()),
                        Integer.parseInt(initiative.getTarget_amount()), initiative.getCreated_by(), initiative.getRef_code());

        initiativeCall.enqueue(new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, response.message());
                    if (!response.body().isError()) {
                        InitiativeRepository.getInstance().insert(response.body().getData());
                        interactor.onSuccess(response.body().getData());
                    } else {
                        interactor.onUnsuccess();
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<Initiative>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                interactor.onError(null);
                call.cancel();
            }
        });
    }

    //endregion

    //region editInitiative

    /**
     *
     * @param id
     * @param name
     * @param createAt
     * @param targetDate
     * @param targetTime
     * @param description
     * @param targetArea
     * @param location
     * @param imagen
     * @param targetAmount
     * @param created_by
     * @param ref_code
     */
    public void editInitiative(final long id, final String name, final String createAt, final String targetDate, final String targetTime, final String description,
                               final String targetArea, final String location, final Bitmap imagen,
                               final String targetAmount, final String created_by, final String ref_code) {
        if (isNotValidInitiative(targetDate, targetTime, targetArea, location))
            return;

        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            editToAPI(id,name,createAt,targetDate,targetTime,description,targetArea,location,imagen,targetAmount,created_by,ref_code);
        } else {
            editToLocal(id,name,createAt,targetDate,targetTime,description,targetArea,location,imagen,targetAmount,created_by,ref_code);
        }

    }

    /**
     *
     * @param id
     * @param name
     * @param createAt
     * @param targetDate
     * @param targetTime
     * @param description
     * @param targetArea
     * @param location
     * @param imagen
     * @param targetAmount
     * @param created_by
     * @param ref_code
     */
    private void editToLocal(final long id, final String name, final String createAt, final String targetDate, final String targetTime, final String description,
                             final String targetArea, final String location, final Bitmap imagen,
                             final String targetAmount, final String created_by, final String ref_code) {

        Initiative editInitiative = new Initiative(id, name, createAt, String.format("%s %s", targetDate, targetTime),
                description, targetArea, location, imagen, targetAmount, created_by, ref_code,
                false, false);
        InitiativeRepository.getInstance().update(editInitiative);
        Initiative initiative = InitiativeRepository.getInstance().getInitiative(editInitiative.getId(), false);

        if(initiative != null) {
            interactor.onSuccess(initiative);
        } else {
            interactor.onUnsuccess();
        }
    }

    /**
     *
     * @param id
     * @param name
     * @param createAt
     * @param targetDate
     * @param targetTime
     * @param description
     * @param targetArea
     * @param location
     * @param imagen
     * @param targetAmount
     * @param created_by
     * @param ref_code
     */
    private void editToAPI(final long id, final String name, final String createAt, final String targetDate, final String targetTime, final String description,
                           final String targetArea, final String location, final Bitmap imagen,
                           final String targetAmount, final String created_by, final String ref_code) {

        Call<APIResponse<Initiative>> initiativeCall =
                initiativeService.putInitiative(name, String.format("%s %s", targetDate, targetTime), description, targetArea, location,
                        new Converters().fromBitmap(imagen), Integer.parseInt(targetAmount), id);

        initiativeCall.enqueue(new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, response.message());
                    if (!response.body().isError()) {
                        InitiativeRepository.getInstance().update(response.body().getData());
                        interactor.onSuccess(response.body().getData());
                    } else {
                        interactor.onUnsuccess();
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<Initiative>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                interactor.onError(null);
                call.cancel();
            }
        });
    }
    //endregion

    //region load Countries and TargetArea

    /**
     * Load all countries from DB
     */
    public void loadCountries(){
        List<Countries> countries = SettingsRepository.getInstance().getListCountries();

        interactor.setCountries(countries);
    }

    /**
     * Load all targetArea from DB
     */
    public void loadTargetArea() {
        List<TargetArea> targetAreas = SettingsRepository.getInstance().getListTargetArea();

        interactor.setTargetArea(targetAreas);
    }

    //endregion

    //region Utils

    private boolean isNotValidInitiative(String name, String targetDate, String targetTime, String targetArea, String location, String targetAmount) {
        if(TextUtils.isEmpty(name)) {
            interactor.onNameEmpty();
            return true;
        }
        if(TextUtils.isEmpty(location)) {
            interactor.onLocationEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetArea)) {
            interactor.onTargetAreaEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetDate)) {
            interactor.onTargetDateEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetTime)) {
            interactor.onTargetTimeEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetAmount)) {
            interactor.onTargetAmountEmpty();
            return true;
        }
        if(!CommonUtils.isInitiativeNameValid(name)) {
            interactor.onNameFormatError();
            return true;
        }
        if(!CommonUtils.isTargetDateValid(targetDate)) {
            interactor.onTargetDateBeforeNowError();
            return true;
        }
        if(Integer.parseInt(targetAmount) < 0) {
            interactor.onTargetAmountFormatError();
            return true;
        }
        return false;
    }

    private boolean isNotValidInitiative(String targetDate, String targetTime, String targetArea, String location) {
        if(TextUtils.isEmpty(location))
        {
            interactor.onLocationEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetArea))
        {
            interactor.onTargetAreaEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetDate))
        {
            interactor.onTargetDateEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetTime))
        {
            interactor.onTargetTimeEmpty();
            return true;
        }
        if(!CommonUtils.isTargetDateValid(targetDate))
        {
            interactor.onTargetDateBeforeNowError();
            return true;
        }
        return false;
    }

    //endregion
}
