package com.douglas.jointlyapp.ui.sync;

import android.util.Log;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.ui.JointlyApplication;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Entity thread that make sync to the API
 */
public class SyncToAPI extends FutureTask<Boolean> {

    public SyncToAPI(Callable<Boolean> callable) {
        super(callable);
    }

    @Override
    public void run() {
        boolean result = syncDataToAPI();
        JointlyApplication.setIsSyncronized(result);
    }

    /**
     * Make sync to the API
     * @return boolean
     */
    public boolean syncDataToAPI() {

        List<UserFollowUser> userFollowUsers = UserRepository.getInstance().getListUserFollowToSync(true, false);
        List<UserReviewUser> userReviewUsers = UserRepository.getInstance().getListUserReviewsToSync(true, false);
        List<UserJoinInitiative> userJoinInitiatives = InitiativeRepository.getInstance().getListUsersJoinedToSync(true, false);
        List<Initiative> initiatives = InitiativeRepository.getInstance().getListInitiativeToSync(true, false);
        List<User> users = UserRepository.getInstance().getListUserToSync(false);

        //TODO descomentar
        Call<APIResponse<UserFollowUser>> userFollowCall = Apis.getInstance().getJointlyService().syncUserFollowToAPI(userFollowUsers);
        try {
            Response<APIResponse<UserFollowUser>> userFollowResponse = userFollowCall.execute();
            if (userFollowResponse.isSuccessful() && userFollowResponse.body() != null) {
                if (!userFollowResponse.body().isError()) {
                    Log.e("TAG", "--------------------> Sincronizado UserFollowUser con exito <--------------------");
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

        Call<APIResponse<UserReviewUser>> userReviewCall = Apis.getInstance().getJointlyService().syncUserReviewToAPI(userReviewUsers);
        try {
            Response<APIResponse<UserReviewUser>> userReviewResponse = userReviewCall.execute();
            if (userReviewResponse.isSuccessful() && userReviewResponse.body() != null) {
                if (!userReviewResponse.body().isError()) {
                    Log.e("TAG", "--------------------> Sincronizado UserReviewUser con exito <------------------------");
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

        Call<APIResponse<UserJoinInitiative>> userJoinCall = Apis.getInstance().getJointlyService().syncUserJoinInitiativeToAPI(userJoinInitiatives);
        try {
            Response<APIResponse<UserJoinInitiative>> userJoinResponse = userJoinCall.execute();
            if (userJoinResponse.isSuccessful() && userJoinResponse.body() != null) {
                if (!userJoinResponse.body().isError()) {
                    Log.e("TAG", "--------------------> Sincronizado UserJoinInitiative con exito <------------------------");
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

        Call<APIResponse<Initiative>> initiativeCall = Apis.getInstance().getJointlyService().syncInitiativeToAPI(initiatives);
        try {
            Response<APIResponse<UserJoinInitiative>> userJoinResponse = userJoinCall.execute();
            if (userJoinResponse.isSuccessful() && userJoinResponse.body() != null) {
                if (!userJoinResponse.body().isError()) {
                    Log.e("TAG", "--------------------> Sincronizado UserJoinInitiative con exito <------------------------");
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

        Call<APIResponse<User>> userCall = Apis.getInstance().getJointlyService().syncUserToApi(users);
        try {
            Response<APIResponse<UserJoinInitiative>> userJoinResponse = userJoinCall.execute();
            if (userJoinResponse.isSuccessful() && userJoinResponse.body() != null) {
                if (!userJoinResponse.body().isError()) {
                    Log.e("TAG", "--------------------> Sincronizado UserJoinInitiative con exito <------------------------");
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


        return true;
    }
}
