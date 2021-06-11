package com.douglas.jointlyapp.ui.reviewdialog;

import android.util.Log;

import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.ui.JointlyApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDialogInteractorImpl {

    interface ReviewInteractor {
        void onSuccessSendMessage(UserReviewUser userReviewUser);
        void onError(String message);
    }

    private final ReviewInteractor interactor;

    public ReviewDialogInteractorImpl(ReviewInteractor interactor) {
        this.interactor = interactor;
    }

    //region sendReview

    public void sendReview(final UserReviewUser userReviewUser) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            sendReviewToAPI(userReviewUser);
        } else {
            sendReviewToLocal(userReviewUser);
        }
    }

    private void sendReviewToLocal(UserReviewUser userReviewUser) {
        userReviewUser.setIs_sync(false);
        long result = UserRepository.getInstance().insertUserReview(userReviewUser);

        if(result != 0) {
            interactor.onSuccessSendMessage(userReviewUser);
        } else {
            interactor.onError(null);
        }
    }

    private void sendReviewToAPI(UserReviewUser userReviewUser) {
        Call<APIResponse<UserReviewUser>> apiResponseCall = Apis.getInstance().getUserService().postUserReview(userReviewUser);
        apiResponseCall.enqueue(new Callback<APIResponse<UserReviewUser>>() {
            @Override
            public void onResponse(Call<APIResponse<UserReviewUser>> call, Response<APIResponse<UserReviewUser>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        Log.e("TAG-------------> ", String.valueOf(response.body().getData()));
                        UserReviewUser reviewUser = response.body().getData();
                        UserRepository.getInstance().insertUserReview(reviewUser);
                        interactor.onSuccessSendMessage(reviewUser);
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserReviewUser>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                apiResponseCall.cancel();
            }
        });
    }

    //endregion
}
