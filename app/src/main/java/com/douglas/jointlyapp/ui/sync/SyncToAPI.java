package com.douglas.jointlyapp.ui.sync;

import android.os.AsyncTask;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;

import java.util.List;

public class SyncToAPI extends AsyncTask<Void,Void, Boolean> {


    @Override
    protected Boolean doInBackground(Void... voids) {
        return syncDataToAPI();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        super.onPostExecute(aBoolean);
    }


    public boolean syncDataToAPI() {

        List<UserFollowUser> userFollowUsers = UserRepository.getInstance().getListUserFollowDeleted(true);
        List<UserReviewUser> userReviewUsers = UserRepository.getInstance().getListUserReviewsDeleted(true);
        List<UserJoinInitiative> userJoinInitiatives = InitiativeRepository.getInstance().getListUsersJoinedDeleted(true);
        List<Initiative> initiatives = InitiativeRepository.getInstance().getListInitiativeDeleted(true);


        return true;
    }
}
