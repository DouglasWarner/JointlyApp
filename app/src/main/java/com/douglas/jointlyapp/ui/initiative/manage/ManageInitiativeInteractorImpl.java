package com.douglas.jointlyapp.ui.initiative.manage;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

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

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Entity who connect with the APIS and LOCALDB
 */
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
     * @param pathImage
     * @param targetAmount
     * @param created_by
     */
    public void addInitiative(final String name, final String targetDate, final String targetTime,
                              final String description, final String targetArea, final String location,
                              final Uri pathImage, final String targetAmount, final String created_by) {
        if (isNotValidInitiative(name, targetDate, targetTime, targetArea, location, targetAmount))
            return;

        Initiative initiative = new Initiative(name, CommonUtils.getDateNow(), CommonUtils.formatDateToAPI(targetDate, targetTime) , description, targetArea,
                    location, pathImage.toString(), targetAmount, created_by);

        ByteArrayOutputStream byteArrayOutputStream = QRCode.from(initiative.toString()).stream();

        initiative.setRef_code(byteArrayOutputStream.toString());

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
        Call<APIResponse<Initiative>> initiativeCall = initiativeService.postInitiativeWithImage(initiative.getName(), CommonUtils.getDateNow(), initiative.getTarget_date(),
                            initiative.getDescription(), initiative.getTarget_area(), initiative.getLocation(),
                            Integer.parseInt(initiative.getTarget_amount()), initiative.getCreated_by(), initiative.getRef_code(),
                            CommonUtils.uploadFile(Uri.parse(initiative.getImage())));

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
     * @param pathImage
     * @param targetAmount
     * @param created_by
     * @param ref_code
     */
    public void editInitiative(final long id, final String name, final String createAt, final String targetDate, final String targetTime, final String description,
                               final String targetArea, final String location, final Uri pathImage,
                               final String targetAmount, final String created_by, final String ref_code) {
        if (isNotValidInitiative(targetDate, targetTime, targetArea, location))
            return;

        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            editToAPI(id,name,createAt,targetDate,targetTime,description,targetArea,location,pathImage,targetAmount,created_by,ref_code);
        } else {
            editToLocal(id,name,createAt,targetDate,targetTime,description,targetArea,location,pathImage,targetAmount,created_by,ref_code);
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
     * @param pathImage
     * @param targetAmount
     * @param created_by
     * @param ref_code
     */
    private void editToLocal(long id, String name, String createAt, String targetDate, String targetTime, String description,
                             String targetArea, String location, Uri pathImage,
                             String targetAmount, String created_by, String ref_code) {

        Initiative editInitiative = new Initiative(id, name, createAt, CommonUtils.formatDateToAPI(targetDate,targetTime),
                description, targetArea, location, pathImage.toString(), targetAmount, created_by, ref_code,
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
     * @param pathImagen
     * @param targetAmount
     * @param created_by
     * @param ref_code
     */
    private void editToAPI(long id, String name, String createAt, String targetDate, String targetTime, String description,
                           String targetArea, String location, Uri pathImagen,
                           String targetAmount, String created_by, String ref_code) {
        Call<APIResponse<Initiative>> initiativeCall = initiativeService.putInitiativeWithImage(name, CommonUtils.formatDateToAPI(targetDate, targetTime), description, targetArea, location,
                Integer.parseInt(targetAmount), CommonUtils.uploadFile(pathImagen), id);

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
