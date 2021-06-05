package com.douglas.jointlyapp.ui.initiative.manage;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.Converters;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.InitiativeService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.douglas.jointlyapp.ui.utils.service.Apis;
import com.douglas.jointlyapp.ui.utils.service.Client;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageInitiativeInteractorImpl {

    private static final String TAG = "ManageInitiativeInteractorImpl";

    interface ManageInitiativeInteractor
    {
        void onNameEmpty();
        void onNameFormatError();
        void onLocationEmpty();
        void onTargetAreaEmpty();
        void onTargetDateEmpty();
        void onTargetDateBeforeNowError();
        void onTargetTimeEmpty();
        void onTargetAmountEmpty();
        void onTargetAmountFormatError();

        void onCannotDeleted();
        void onSuccessDeleted();

        void onSuccessLoad(Initiative initiative);
        void onSuccess(Initiative initiative);
        void onUnsuccess(String message);

        void onError(String message);
    }

    private final ManageInitiativeInteractor interactor;
    private final InitiativeService initiativeService;

    public ManageInitiativeInteractorImpl(ManageInitiativeInteractor manageInitiativeInteractor) {
        this.interactor = manageInitiativeInteractor;
        this.initiativeService = Apis.getInstance().getInitiativeService();
    }

    public void addInitiative(final String name, final String targetDate, final String targetTime,
                              final String description, final String targetArea, final String location, final Bitmap imagen, final String targetAmount,
                              final String status, final String created_by)
    {
        if (isNotValidInitiative(name, targetDate, targetTime, targetArea, location, targetAmount))
            return;

        if(JointlyApplication.getConnection()) {
            insertToAPI(name, targetDate, targetTime, description, targetArea, location, imagen, targetAmount, status, created_by);
        } else {
            insertToLocal(name, targetDate, targetTime, description, targetArea, location, imagen, targetAmount, status, created_by);
        }
    }

    public void editInitiative(final long id, final String name, final String createAt, final String targetDate, final String targetTime, final String description,
                               final String targetArea, final String location, final Bitmap imagen, final String targetAmount,
                               final String status, final String created_by, final String ref_code)
    {
        if (isNotValidInitiative(targetDate, targetTime, targetArea, location))
            return;

        if(JointlyApplication.getConnection()) {
            updateToAPI(id,name,createAt,targetDate,targetTime,description,targetArea,location,imagen,targetAmount,status,created_by,ref_code);
        } else {
            updateToLocal(id,name,createAt,targetDate,targetTime,description,targetArea,location,imagen,targetAmount,status,created_by,ref_code);
        }

    }

    public void deleteInitiative(final Initiative initiative)
    {
        List<User> userList = UserRepository.getInstance().getListUserJoined(initiative.getId());

        if(!userList.isEmpty())
        {
            interactor.onCannotDeleted();
            return;
        }

        if(JointlyApplication.getConnection()) {
            deleteToAPI(initiative);
        } else {
            deleteToLocal(initiative);
        }
    }

    private boolean isNotValidInitiative(String name, String targetDate, String targetTime, String targetArea, String location, String targetAmount) {
        if(TextUtils.isEmpty(name))
        {
            interactor.onNameEmpty();
            return true;
        }
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
        if(TextUtils.isEmpty(targetAmount))
        {
            interactor.onTargetAmountEmpty();
            return true;
        }

        if(!CommonUtils.isInitiativeNameValid(name))
        {
            interactor.onNameFormatError();
            return true;
        }

//        if(!CommonUtils.isTargetDateValid(targetDate))
//        {
//            interactor.onTargetDateBeforeNowError();
//            return true;
//        }

        if(Integer.parseInt(targetAmount) < 0)
        {
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

    private void insertToLocal(final String name, final String targetDate, final String targetTime,
                               final String description, final String targetArea, final String location, final Bitmap imagen, final String targetAmount,
                               final String status, final String created_by) {

        //TODO mirar sync cuando vuelva la conexion
        // Quizas ingresando un id negativo si se pudiera
        // y en la API estableciendo que cuando encuentre un id negativo haga lo que tenga que hacer
        InitiativeRepository repository = InitiativeRepository.getInstance();

        Initiative addInitiative = new Initiative(name, CommonUtils.getDateNow(), String.format("%s %s", targetDate, targetTime), description, targetArea,
                                                location, imagen, targetAmount, status, created_by, String.valueOf(new Random().nextInt()));

        int result = (int) repository.insert(addInitiative);

        Initiative initiative = repository.getInitiative(result);

        if(initiative != null) {
            interactor.onSuccess(initiative);
        } else {
            interactor.onUnsuccess(JointlyApplication.getContext().getString(R.string.error_insert_initiative));
        }
    }

    private void insertToAPI(final String name, final String targetDate, final String targetTime,
                             final String description, final String targetArea, final String location, final Bitmap imagen, final String targetAmount,
                             final String status, final String created_by) {

        Call<APIResponse<Initiative>> initiativeCall = initiativeService.postInitiative(name, CommonUtils.getDateNow(), String.format("%s %s", targetDate, targetTime), description, targetArea,
                                                                                    location, new Converters().fromBitmap(imagen), Integer.parseInt(targetAmount),
                                                                                    created_by);

        initiativeCall.enqueue(new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                Log.e(TAG, response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        interactor.onSuccess(response.body().getData().get(0));
                    } else {
                        interactor.onUnsuccess(response.body().getMessage());
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

    private void updateToLocal(final long id, final String name, final String createAt, final String targetDate, final String targetTime, final String description,
                               final String targetArea, final String location, final Bitmap imagen, final String targetAmount,
                               final String status, final String created_by, final String ref_code) {
        InitiativeRepository repository = InitiativeRepository.getInstance();

        Initiative editInitiative = new Initiative(id, name, createAt, String.format("%s %s", targetDate, targetTime), description, targetArea, location, imagen, targetAmount, status, created_by, ref_code);

        repository.update(editInitiative);

        Initiative initiative = repository.getInitiative(editInitiative.getId());

        if(initiative != null) {
            interactor.onSuccess(initiative);
        } else {
            interactor.onUnsuccess(JointlyApplication.getContext().getString(R.string.error_update_initiative));
        }
    }

    private void updateToAPI(final long id, final String name, final String createAt, final String targetDate, final String targetTime, final String description,
                             final String targetArea, final String location, final Bitmap imagen, final String targetAmount,
                             final String status, final String created_by, final String ref_code) {

        Call<APIResponse<Initiative>> initiativeCall = initiativeService.putInitiative(name, String.format("%s %s", targetDate, targetTime), description, targetArea, location,
                                                                                new Converters().fromBitmap(imagen), Integer.parseInt(targetAmount), id);

        initiativeCall.enqueue(new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                Log.e(TAG, response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        interactor.onSuccess(response.body().getData().get(0));
                    } else {
                        interactor.onUnsuccess(response.body().getMessage());
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

    private void deleteToLocal(final Initiative initiative) {
        InitiativeRepository repository = InitiativeRepository.getInstance();

        repository.delete(initiative);

        interactor.onSuccessDeleted();
    }

    private void deleteToAPI(final Initiative initiative) {
        Call<APIResponse<Initiative>> initiativeCall = initiativeService.delInitiative(initiative.getId());

        initiativeCall.enqueue(new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                Log.e(TAG, response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        interactor.onSuccess(response.body().getData().get(0));
                    } else {
                        interactor.onUnsuccess(response.body().getMessage());
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
}
