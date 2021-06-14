package com.douglas.jointlyapp.ui.sync;

import android.util.Log;

import com.douglas.jointlyapp.data.model.Countries;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.TargetArea;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.SettingsRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.ui.JointlyApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Entity thread that make sync with the API
 */
public class SyncFromAPI extends FutureTask<Boolean> {

    public SyncFromAPI(Callable<Boolean> callable) {
        super(callable);
    }

    @Override
    public void run() {
        boolean result = syncDataFromAPI();
        JointlyApplication.setIsSyncronized(result);
    }

    /**
     * Make sync from API
     * @return boolean
     */
    private boolean syncDataFromAPI() {

        Call<APIResponse<List<User>>> userCall = Apis.getInstance().getUserService().getListUser();
        try {
            Log.e("TAG", userCall.timeout().timeoutNanos()+"");
            Response<APIResponse<List<User>>> userResponse = userCall.execute();
            if(userResponse.isSuccessful() && userResponse.body() != null) {
                if(!userResponse.body().isError()) {
                    Log.e("TAG-------------> ", String.valueOf(userResponse.body().getData().get(0)));
                    UserRepository.getInstance().syncUserFromAPI(userResponse.body().getData());
                }
            } else {
                userCall.cancel();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            userCall.cancel();
            return false;
        }

        Call<APIResponse<List<Initiative>>> initiativeCall = Apis.getInstance().getInitiativeService().getListInitiative();
        try {
            Response<APIResponse<List<Initiative>>> initiativeResponse = initiativeCall.execute();
            if(initiativeResponse.isSuccessful() && initiativeResponse.body() != null) {
                if(!initiativeResponse.body().isError()) {
                    InitiativeRepository.getInstance().syncInitiativeFromAPI(initiativeResponse.body().getData());
                }
            } else {
                initiativeCall.cancel();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            initiativeCall.cancel();
            return false;
        }

        Call<APIResponse<List<UserFollowUser>>> userFollowCall = Apis.getInstance().getUserService().getListUserFollow();
        try {
            Response<APIResponse<List<UserFollowUser>>> userFollowResponse = userFollowCall.execute();
            if (userFollowResponse.isSuccessful() && userFollowResponse.body() != null) {
                if (!userFollowResponse.body().isError()) {
                    UserRepository.getInstance().syncUserFollowFromAPI(userFollowResponse.body().getData());
                }
            } else {
                userFollowCall.cancel();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            userFollowCall.cancel();
            return false;
        }

        Call<APIResponse<List<UserReviewUser>>> userReviewCall = Apis.getInstance().getUserService().getListUserReview();
        try {
            Response<APIResponse<List<UserReviewUser>>> userReviewResponse = userReviewCall.execute();
            if (userReviewResponse.isSuccessful() && userReviewResponse.body() != null) {
                if (!userReviewResponse.body().isError()) {
                    UserRepository.getInstance().syncUserReviewFromAPI(userReviewResponse.body().getData());
                }
            } else {
                userReviewCall.cancel();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            userReviewCall.cancel();
            return false;
        }

        Call<APIResponse<List<UserJoinInitiative>>> userJoinCall = Apis.getInstance().getInitiativeService().getListUserJoined();
        try {
            Response<APIResponse<List<UserJoinInitiative>>> userJoinResponse = userJoinCall.execute();
            if (userJoinResponse.isSuccessful() && userJoinResponse.body() != null) {
                if (!userJoinResponse.body().isError()) {
                    InitiativeRepository.getInstance().syncUserJoinFromAPI(userJoinResponse.body().getData());
                }
            } else {
                userJoinCall.cancel();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            userJoinCall.cancel();
            return false;
        }

        Call<APIResponse<List<TargetArea>>> targetAreaCall = Apis.getInstance().getJointlyService().getListTargetArea();
        targetAreaCall.enqueue(new Callback<APIResponse<List<TargetArea>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<TargetArea>>> call, Response<APIResponse<List<TargetArea>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        SettingsRepository.getInstance().syncTargetAreaFromAPI(response.body().getData());
                    }
                } else {
                    targetAreaCall.cancel();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<TargetArea>>> call, Throwable t) {
                targetAreaCall.cancel();
            }
        });

        Call<APIResponse<List<Countries>>> countriesCall = Apis.getInstance().getJointlyService().getListCountries();
        countriesCall.enqueue(new Callback<APIResponse<List<Countries>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Countries>>> call, Response<APIResponse<List<Countries>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        SettingsRepository.getInstance().syncCountriesFromAPI(response.body().getData());
                    }
                } else {
                    countriesCall.cancel();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<Countries>>> call, Throwable t) {
                countriesCall.cancel();
            }
        });

        return true;
    }
}
