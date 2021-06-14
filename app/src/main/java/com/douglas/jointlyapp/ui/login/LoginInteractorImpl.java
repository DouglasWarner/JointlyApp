package com.douglas.jointlyapp.ui.login;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Entity who connect with the APIS and LOCALDB
 */
public class LoginInteractorImpl {

    public interface LoginInteractor {
        void onEmailEmptyError();
        void onPasswordEmptyError();
        void onEmailFormatError();
        void onPasswordFormatError();
        void onAuthenticationError(String message);
        void onSuccess();

        void onError(String message);
    }

    private LoginInteractor interactor;
    private UserService userService;
    private FirebaseAuth firebaseAuth;

    public LoginInteractorImpl(LoginInteractor loginInteractor) {
        this.interactor = loginInteractor;
        this.userService = Apis.getInstance().getUserService();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    //TODO mirar el login sin internet a ver que hace

    /**
     * Function that check credentials to firebase and then get the user
     * @param email
     * @param password
     */
    public void validateCredentials(final String email, final String password) {
        if(!checkFieldsValid(email, password)) return;

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FirebaseUser firebaseUser = task.getResult().getUser();
                FirebaseDatabase.getInstance().getReference().child("token").child(task.getResult().getUser().getUid()).setValue(JointlyApplication.getToken());
                if(JointlyApplication.isIsSyncronized()) {
                    getUserFromAPI(task.getResult().getUser().getEmail());
                } else {
                    User localUser = UserRepository.getInstance().getUser(firebaseUser.getEmail());
                    if(localUser == null) {
                        User user = new User(firebaseUser.getEmail(), firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhoneNumber(),
                                firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : "", "",
                                "", CommonUtils.getDateFromLong(firebaseUser.getMetadata().getCreationTimestamp()));

                        JointlyApplication.setCurrentSignInUser(user);

                        UserRepository.getInstance().insert(user);
                    }
                    interactor.onSuccess();
                }
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    interactor.onAuthenticationError("USER NOT FOUND");
                } catch (FirebaseAuthUserCollisionException e) {
                    interactor.onAuthenticationError("USER ALREADY EXISTS");
                } catch (Exception e) {
                    interactor.onAuthenticationError(task.getException().getMessage());
                }
            }
        });
    }

    //TODO quizas tenga que hacer el post user, aunque no creo. Estudiar el caso antes de nada
    /**
     * Get User From API
     * @param email
     */
    private void getUserFromAPI(String email) {
        Call<APIResponse<User>> callUser = userService.getUserByEmail(email);
        callUser.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Call<APIResponse<User>> call, Response<APIResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        interactor.onSuccess();
                        JointlyApplication.setCurrentSignInUser(response.body().getData());
                    } else {
                        interactor.onAuthenticationError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<User>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                callUser.cancel();
            }
        });
    }

    /**
     * Function that check if the fields introduced are validate
     * @param email
     * @param password
     * @return false if is not valid, true if is valid
     */
    private boolean checkFieldsValid(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            interactor.onEmailEmptyError();
            return false;
        }
        if(TextUtils.isEmpty(password)) {
            interactor.onPasswordEmptyError();
            return false;
        }
        if(!CommonUtils.isEmailValid(email)) {
            interactor.onEmailFormatError();
            return false;
        }
        if(!CommonUtils.isPasswordValid(password)) {
            interactor.onPasswordFormatError();
            return false;
        }
        return true;
    }

    /**
     * doLoginGoogle
     * @param idToken
     */
    void doLoginGoogle(final String idToken, final Activity view) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(view, (OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        //save the token of devi with his UUID on database firebase
                        FirebaseDatabase.getInstance().getReference().child("token").child(task.getResult().getUser().getUid()).setValue(JointlyApplication.getToken());

                        FirebaseUser user = task.getResult().getUser();
                        if(JointlyApplication.isIsSyncronized()) {
                            insertUserToAPI(user.getEmail(),user.getUid(),user.getDisplayName(), user.getPhotoUrl(), user.getPhoneNumber(),
                                    "", "", CommonUtils.getDateFromLong(user.getMetadata().getCreationTimestamp()));
                        } else {
                            User localUser = new User(user.getEmail(), user.getUid(), user.getDisplayName(), "", user.getPhotoUrl().toString(),
                                    "", "", CommonUtils.getDateFromLong(user.getMetadata().getCreationTimestamp()));
                            UserRepository.getInstance().insert(localUser);
                            interactor.onSuccess();
                            JointlyApplication.setCurrentSignInUser(localUser);
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        interactor.onError(task.getException().getMessage());
                    }
                });
    }

    /**
     * doLoginFacebook
     * @param accessToken
     */
    void doLoginFacebook(final AccessToken accessToken, final Activity view) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(view, (OnCompleteListener<AuthResult>) task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference().child("token").child(task.getResult().getUser().getUid()).setValue(JointlyApplication.getToken());
                Log.d("TAG", "signInWithCredential:success");
                FirebaseUser user = task.getResult().getUser();
                if(JointlyApplication.isIsSyncronized()) {
                    insertUserToAPI(user.getEmail(),user.getUid(),user.getDisplayName(), user.getPhotoUrl(), user.getPhoneNumber(),
                            "", "", CommonUtils.getDateFromLong(user.getMetadata().getCreationTimestamp()));
                } else {
                    User localUser = new User(user.getEmail(), user.getUid(), user.getDisplayName(), "", user.getPhotoUrl().toString(),
                            "", "", CommonUtils.getDateFromLong(user.getMetadata().getCreationTimestamp()));
                    UserRepository.getInstance().insert(localUser);
                    interactor.onSuccess();
                    JointlyApplication.setCurrentSignInUser(localUser);
                }

            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithCredential:failure", task.getException());
                interactor.onError(task.getException().getMessage());
            }
        });
    }

    /**
     * Save the user to the API. If exists do anything
     * @param email
     * @param password
     * @param userName
     */
    private void insertUserToAPI(String email, String password, String userName, Uri image, String phone, String location, String description, String created_at) {
        Call<APIResponse<User>> call = userService.postUser(email, password, userName, " ", "", " ",
                CommonUtils.formatDateToAPI(created_at.split(" ")[0], created_at.split(" ")[1]));
        call.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Call<APIResponse<User>> call, Response<APIResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    UserRepository.getInstance().insert(response.body().getData());
                    JointlyApplication.setCurrentSignInUser(response.body().getData());
                    interactor.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<User>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                call.cancel();
            }
        });
    }
}
