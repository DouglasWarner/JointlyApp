package com.douglas.jointlyapp.ui.showuserprofile;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Entity who connect with the APIS and LOCALDB
 */
public class ShowUserProfileInteractorImpl {

    interface ProfileInteractor {
        void onSuccessUnFollow();
        void onSuccessFollow();
        void onUserStateFollow(boolean follow);
        void onRatingUser(float average);

        void onError(String message);
    }

    private ProfileInteractor interactor;
    private static UserService userService;

    public ShowUserProfileInteractorImpl(ProfileInteractor interactor) {
        this.interactor = interactor;
        userService = Apis.getInstance().getUserService();
    }

    //region loadUserStateFollow

    /**
     *
     * @param user
     */
    public void loadUserStateFollow(User user) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            loadUserStateFollowFromAPI(user);
        } else {
            loadUserStateFollowFromLocal(user);
        }
    }

    /**
     *
     * @param userFollow
     */
    private void loadUserStateFollowFromLocal(User userFollow) {
        String user = JointlyApplication.getCurrentSignInUser().getEmail();
        UserFollowUser userFollowUser = UserRepository.getInstance().getUserFollowUser(user, userFollow.getEmail(), false);

        interactor.onUserStateFollow(userFollowUser != null);
    }

    /**
     *
     * @param userFollow
     */
    private void loadUserStateFollowFromAPI(User userFollow) {
        String user = JointlyApplication.getCurrentSignInUser().getEmail();

        Call<APIResponse<List<User>>> userFollowCall = userService.getUserFollowed(user);
        userFollowCall.enqueue(new Callback<APIResponse<List<User>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<User>>> call, Response<APIResponse<List<User>>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        interactor.onUserStateFollow(response.body().getData().stream().anyMatch(x-> x.getEmail().equals(userFollow.getEmail())));
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<User>>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                userFollowCall.cancel();
            }
        });
    }

    //endregion

    //region manageFollowUser

    /**
     *
     * @param userFollow
     */
    public void manageFollowUser(User userFollow) {
        String user = JointlyApplication.getCurrentSignInUser().getEmail();
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            manageFollowUserFromAPI(user, userFollow);
        } else {
            manageFollowUserFromLocal(user, userFollow);
        }
    }

    /**
     *
     * @param userFollow
     */
    private void manageFollowUserFromLocal(String user, User userFollow) {
        UserFollowUser userFollowUser = UserRepository.getInstance().getUserFollowUser(user, userFollow.getEmail(), false);

        if(userFollowUser != null) {
            UserRepository.getInstance().updateUserFollowed(new UserFollowUser(user, userFollow.getEmail(), true, false));
            interactor.onSuccessUnFollow();
            unSubscribeNotificationFollow(userFollow.getEmail());
        } else {
            UserRepository.getInstance().upsertUserFollowUser(new UserFollowUser(user, userFollow.getEmail(), false, false));
            interactor.onSuccessFollow();
            subscribeNotificationFollow(userFollow.getEmail());
        }
    }

    /**
     *
     * @param user
     * @param userFollow
     */
    private void manageFollowUserFromAPI(String user, User userFollow) {
        Call<APIResponse<UserFollowUser>> insertUserFollowCall = userService.postUserFollow(user, userFollow.getEmail());
        Call<APIResponse<UserFollowUser>> deleteUserFollowCall = userService.delUserFollow(user, userFollow.getEmail());

        insertUserFollowCall.enqueue(new Callback<APIResponse<UserFollowUser>>() {
            @Override
            public void onResponse(Call<APIResponse<UserFollowUser>> call, Response<APIResponse<UserFollowUser>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        Log.e("TAG-------------> ", String.valueOf(response.body().getData()));
                        UserFollowUser followUser = response.body().getData();
                        if (followUser != null) {
                            UserRepository.getInstance().insertUserFollowed(followUser);
                            interactor.onSuccessFollow();
                            subscribeNotificationFollow(followUser.getUser_follow());
                        }
                    } else {
                        deleteUserFollowCall.enqueue(deleteUserFollowCallBack(user, userFollow.getEmail()));
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserFollowUser>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
            }
        });
    }

    /**
     * call to delete user to API
     * @param user
     * @param userFollow
     * @return Callback<APIResponse<UserFollowUser>>
     */
    private Callback<APIResponse<UserFollowUser>> deleteUserFollowCallBack(String user, String userFollow) {
        return new Callback<APIResponse<UserFollowUser>>() {
            @Override
            public void onResponse(Call<APIResponse<UserFollowUser>> call, Response<APIResponse<UserFollowUser>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        UserRepository.getInstance().deleteUserFollowed(new UserFollowUser(user, userFollow, true, true));
                        interactor.onSuccessUnFollow();
                        unSubscribeNotificationFollow(userFollow);
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserFollowUser>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
            }
        };
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

    /**
     * call to load user rating review from API
     * @param user
     */
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

    private void subscribeNotificationFollow(String userFollow) {
        FirebaseMessaging.getInstance().subscribeToTopic(userFollow).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(JointlyApplication.getContext(), "Suscrito a "+ userFollow, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unSubscribeNotificationFollow(String userFollow) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(userFollow).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(JointlyApplication.getContext(), "Desuscrito a "+ userFollow, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
