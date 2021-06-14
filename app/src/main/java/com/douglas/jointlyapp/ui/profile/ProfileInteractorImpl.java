package com.douglas.jointlyapp.ui.profile;

import android.net.Uri;
import android.util.Log;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Entity who connect with the APIS and LOCALDB
 */
public class ProfileInteractorImpl {

    interface ProfileInteractor {
        void onRatingUser(float average);
        void onUpdateImage();
        void onError(String message);
    }

    private ProfileInteractor interactor;
    private static UserService userService;

    public ProfileInteractorImpl(ProfileInteractor interactor) {
        this.interactor = interactor;
    }

    //region updateImage

    /**
     * updateUser
     * @param user
     */
    public void updateImage(final User user, final Uri image) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            updateImageToAPI(user, image);
        } else {
            UserRepository.getInstance().update(user);
        }
    }

    /**
     * updateImageToAPI
     * @param user
     * @param image
     */
    private void updateImageToAPI(User user, Uri image) {
        Call<APIResponse<User>> call = Apis.getInstance().getUserService().putUserWithImage(user.getEmail(),user.getPassword(),user.getName(),user.getPhone(),
                user.getDescription(),user.getDescription(), CommonUtils.uploadFile(image),user.getId());

        call.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Call<APIResponse<User>> call, Response<APIResponse<User>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        UserRepository.getInstance().update(response.body().getData());
                        interactor.onUpdateImage();
                    } else {
                        interactor.onError(null);
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<User>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
                interactor.onError(null);
                call.cancel();
            }
        });
    }

    //endregion

    //region loadRatingUser

    /**
     *
     * @param user
     */
    public void loadRatingUser(final String user) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            loadRatingUserFromAPI(user);
        } else {
            loadRatingUserFromLocal(user);
        }
    }

    /**
     *
     * @param user
     */
    private void loadRatingUserFromLocal(String user) {
        List<UserReviewUser> userReviewUsers = UserRepository.getInstance().getListUserReviews(user, false);

        if(userReviewUsers.isEmpty()) {
            interactor.onRatingUser(0);
        } else {
            float average = CommonUtils.getAverage(userReviewUsers);
            interactor.onRatingUser(average);
        }
    }

    private void loadRatingUserFromAPI(String user) {
        Call<APIResponse<List<UserReviewUser>>> apiResponseCall = Apis.getInstance().getUserService().getListUserReview(user);
        apiResponseCall.enqueue(new Callback<APIResponse<List<UserReviewUser>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<UserReviewUser>>> call, Response<APIResponse<List<UserReviewUser>>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        Log.e("TAG-------------> ", String.valueOf(response.body().getData()));
                        List<UserReviewUser> reviewUser = response.body().getData();
                        if(reviewUser.isEmpty()) {
                            interactor.onRatingUser(0);
                        } else {
                            float average = CommonUtils.getAverage(reviewUser);
                            interactor.onRatingUser(average);
                        }
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<UserReviewUser>>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                apiResponseCall.cancel();
            }
        });
    }

    //endregion
}
