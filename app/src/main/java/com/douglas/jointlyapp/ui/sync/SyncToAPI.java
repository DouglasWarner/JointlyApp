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
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.services.Apis;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import retrofit2.Call;
import retrofit2.Response;

public class SyncToAPI extends FutureTask<Boolean> {

    public SyncToAPI(Callable<Boolean> callable) {
        super(callable);
    }

    @Override
    public void run() {
        boolean result = syncDataToAPI();
        JointlyApplication.setIsSyncronized(result);
        JointlyApplication.setConnection(result);
    }

    public boolean syncDataToAPI() {

        List<UserFollowUser> userFollowUsers = UserRepository.getInstance().getListUserFollowToSync(true, false);
        List<UserReviewUser> userReviewUsers = UserRepository.getInstance().getListUserReviewsToSync(true, false);
        List<UserJoinInitiative> userJoinInitiatives = InitiativeRepository.getInstance().getListUsersJoinedToSync(true, false);
        List<Initiative> initiatives = InitiativeRepository.getInstance().getListInitiativeToSync(true, false);
        List<User> users = UserRepository.getInstance().getListUserToSync(true);

        Call<APIResponse<UserFollowUser>> userFollowCall = Apis.getInstance().getUserService().syncToAPI(userFollowUsers);
        try {
            Response<APIResponse<UserFollowUser>> userFollowResponse = userFollowCall.execute();
            if (userFollowResponse.isSuccessful() && userFollowResponse.body() != null) {
                if (!userFollowResponse.body().isError()) {
                    Log.e("TAG", "-------------Sincronizado con exito---------------");
//                    Toast.makeText(JointlyApplication.getContext(), "Sincronizado UserFollow con Api", Toast.LENGTH_SHORT).show();
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

        return true;
    }
}
