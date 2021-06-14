package com.douglas.jointlyapp.ui.favorite;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
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
public class FavoriteInteractorImpl {

    interface FavoriteInteractor {
        void onNoData();
        void onSuccess(List<User> list);
        void onSuccessUnFollow();
        void onSuccessFollow();

        void onError(String message);
    }

    private FavoriteInteractorImpl.FavoriteInteractor interactor;
    private UserService userService;

    public FavoriteInteractorImpl(FavoriteInteractorImpl.FavoriteInteractor interactor) {
        this.interactor = interactor;
        this.userService = Apis.getInstance().getUserService();
    }

    //region loadData

    public void loadData() {
        String user = JointlyApplication.getCurrentSignInUser().getEmail();

        if (JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            getListfromAPI(user);
        } else {
            getListfromLocal(user);
        }
    }

    /**
     * Get the data from the local DB
     */
    private void getListfromLocal(String email) {
        List<User> listUserFollowed = UserRepository.getInstance().getListUserFollowed(email, false);

        if (listUserFollowed.isEmpty()) {
            interactor.onNoData();
        } else {
            interactor.onSuccess(listUserFollowed);
        }
    }

    /**
     * Get the data from the API Service
     */
    private void getListfromAPI(String email) {
        Call<APIResponse<List<User>>> userFollowCall = userService.getUserFollowed(email);
        userFollowCall.enqueue(new Callback<APIResponse<List<User>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<User>>> call, Response<APIResponse<List<User>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        List<User> list = response.body().getData();
                        if (list.isEmpty()) {
                            interactor.onNoData();
                        } else {
                            interactor.onSuccess(list);
                        }
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

    //region followUser

    public void followUser(final User userFollowed) {
        User user = UserRepository.getInstance().getUser(JointlyPreferences.getInstance().getUser());

        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            manageUserFollowToAPI(user.getEmail(), userFollowed.getEmail());
        } else {
            manageUserFollowToLocal(user, userFollowed);
        }
    }

    /**
     * Inserta o eliminar de la base de datos.
     * Permite que se pueda insertar o eliminar en la misma vista
     * @param user
     * @param userFollowed
     */
    private void manageUserFollowToLocal(User user, User userFollowed) {
        UserFollowUser userFollowUser = UserRepository.getInstance().getUserFollowUser(user.getEmail(), userFollowed.getEmail(), false);

        if(userFollowUser != null) {
            UserRepository.getInstance().updateUserFollowed(new UserFollowUser(user.getEmail(), userFollowed.getEmail(), true, false));
            interactor.onSuccessUnFollow();
            unSubscribeNotificationFollow(userFollowed.getEmail());
        } else {
            UserRepository.getInstance().upsertUserFollowUser(new UserFollowUser(user.getEmail(), userFollowed.getEmail(), false, false));
            interactor.onSuccessFollow();
            subscribeNotificationFollow(userFollowed.getEmail());
        }
    }

    private void manageUserFollowToAPI(String user, String userFollowed) {
        Call<APIResponse<UserFollowUser>> insertUserFollowCall = userService.postUserFollow(user, userFollowed);
        Call<APIResponse<UserFollowUser>> deleteUserFollowCall = userService.delUserFollow(user, userFollowed);

        insertUserFollowCall.enqueue(new Callback<APIResponse<UserFollowUser>>() {
            @Override
            public void onResponse(Call<APIResponse<UserFollowUser>> call, Response<APIResponse<UserFollowUser>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        Log.e("TAG-------------> ", String.valueOf(response.body().getData()));
                        UserFollowUser followUser = response.body().getData();
                        if (followUser != null) {
                            UserRepository.getInstance().insertUserFollowed(followUser);
                            interactor.onSuccessFollow();
                            subscribeNotificationFollow(followUser.getUser_follow());
                        }
                    } else {
                        deleteUserFollowCall.enqueue(deleteUserFollowCallBack(user, userFollowed));
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserFollowUser>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                insertUserFollowCall.cancel();
            }
        });
    }

    private Callback<APIResponse<UserFollowUser>> deleteUserFollowCallBack(String user, String userFollowed) {
        return new Callback<APIResponse<UserFollowUser>>() {
            @Override
            public void onResponse(Call<APIResponse<UserFollowUser>> call, Response<APIResponse<UserFollowUser>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        UserRepository.getInstance().deleteUserFollowed(new UserFollowUser(user, userFollowed, true, true));
                        interactor.onSuccessUnFollow();
                        unSubscribeNotificationFollow(userFollowed);
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
                call.cancel();
            }
        };
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
