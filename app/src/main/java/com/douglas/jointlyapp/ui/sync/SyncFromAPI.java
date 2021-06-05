package com.douglas.jointlyapp.ui.sync;

import android.os.AsyncTask;
import android.util.Log;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.douglas.jointlyapp.ui.utils.service.Apis;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class SyncFromAPI extends AsyncTask<Void,Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        return syncDataFromAPI();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        JointlyApplication.setConnection(aBoolean);

        super.onPostExecute(aBoolean);
    }

    public boolean syncDataFromAPI() {

        Call<APIResponse<User>> userCall = Apis.getInstance().getUserService().getListUser();
        try {
            Log.e("TAG", userCall.timeout().timeoutNanos()+"");
            Response<APIResponse<User>> userResponse = userCall.execute();
            if(userResponse.isSuccessful() && userResponse.body() != null) {
                if(!userResponse.body().isError()) {
                    userResponse.body().getData().forEach(x -> x.setImagen(
                            x.getImagen() == null ? CommonUtils.getImagenUserDefault(JointlyApplication.getContext()) : x.getImagen()
                    ));
                    UserRepository.getInstance().upsertUser(userResponse.body().getData());
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

        Call<APIResponse<Initiative>> initiativeCall = Apis.getInstance().getInitiativeService().getListInitiative();
        try {
            Response<APIResponse<Initiative>> initiativeResponse = initiativeCall.execute();
            if(initiativeResponse.isSuccessful() && initiativeResponse.body() != null) {
                if(!initiativeResponse.body().isError()) {
                    InitiativeRepository.getInstance().deleteAllInitiative();
                    initiativeResponse.body().getData().forEach(x -> x.setImagen(
                            x.getImagen() == null ? CommonUtils.getImagenInitiativeDefault(JointlyApplication.getContext()) : x.getImagen()
                    ));
                    InitiativeRepository.getInstance().upsertInitiative(initiativeResponse.body().getData());
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

        Call<APIResponse<UserJoinInitiative>> userJoinCall = Apis.getInstance().getInitiativeService().getListUserJoined();
        try {
            Response<APIResponse<UserJoinInitiative>> userJoinResponse = userJoinCall.execute();
            if (userJoinResponse.isSuccessful() && userJoinResponse.body() != null) {
                if (!userJoinResponse.body().isError()) {
                    InitiativeRepository.getInstance().deleteAllUserJoin();
                    InitiativeRepository.getInstance().upsertUserJoinInitiative(userJoinResponse.body().getData());
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

        Call<APIResponse<UserFollowUser>> userFollowCall = Apis.getInstance().getUserService().getListUserFollow();
        try {
            Response<APIResponse<UserFollowUser>> userFollowResponse = userFollowCall.execute();
            if (userFollowResponse.isSuccessful() && userFollowResponse.body() != null) {
                if (!userFollowResponse.body().isError()) {
                    UserRepository.getInstance().deleteAllUserFollow();
                    UserRepository.getInstance().upsertUserFollowUser(userFollowResponse.body().getData());
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

        Call<APIResponse<UserReviewUser>> userReviewCall = Apis.getInstance().getUserService().getListUserReview();
        try {
            Response<APIResponse<UserReviewUser>> userReviewResponse = userReviewCall.execute();
            if (userReviewResponse.isSuccessful() && userReviewResponse.body() != null) {
                if (!userReviewResponse.body().isError()) {
                    UserRepository.getInstance().deleteAllUserReview();
                    UserRepository.getInstance().upsertUserReviewUser(userReviewResponse.body().getData());
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

        return true;
    }
}
