package com.douglas.jointlyapp.ui.signup;

import android.text.TextUtils;
import android.util.Log;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Entity who connect with the APIS and LOCALDB
 */
public class SignUpInteractorImpl {

    interface SignUpInteractor {
        void onEmailEmptyError();
        void onPasswordEmptyError();
        void onUserNameEmptyError();
        void onEmailFormatError();
        void onPasswordFormatError();
        void onPasswordNotEqualError();
        void onUserExistsError();
        void onSuccess();
        void onError(String message);
    }

    private SignUpInteractor interactor;
    private UserService userService;
    private FirebaseAuth firebaseAuth;

    public SignUpInteractorImpl(SignUpInteractor signUpInteractor) {
        this.interactor = signUpInteractor;
        this.userService = Apis.getInstance().getUserService();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * registrer an user
     * @param email
     * @param password
     * @param confirmPassword
     * @param userName
     */
    public void addUser(final String email, final String password, final String confirmPassword, final String userName) {
        if (!checkFieldsValid(email, password, confirmPassword, userName)) return;

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                FirebaseDatabase.getInstance().getReference().child("token").child(task.getResult().getUser().getUid()).setValue(JointlyApplication.getToken());
                if(JointlyApplication.isIsSyncronized()) {
                    insertUserToAPI(email, task.getResult().getUser().getUid(), userName);
                } else {
                    User localUser = new User(user.getEmail(), user.getUid(), userName, "", "",
                            "", "",
                            CommonUtils.formatDateToAPI(CommonUtils.getDateFromLong(user.getMetadata().getCreationTimestamp()).split(" ")[0],
                                    CommonUtils.getDateFromLong(user.getMetadata().getCreationTimestamp()).split(" ")[1] ));
                    UserRepository.getInstance().insert(localUser);
                    JointlyApplication.setCurrentSignInUser(localUser);
                    interactor.onSuccess();
                }
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthUserCollisionException e) {
                    interactor.onUserExistsError();
                } catch (Exception e) {
                    interactor.onError(task.getException().getMessage());
                }
                interactor.onError(task.getException().toString());
            }
        });
    }

    /**
     * Save the user to the API
     * @param email
     * @param password
     * @param userName
     */
    private void insertUserToAPI(String email, String password, String userName) {
        Call<APIResponse<User>> call = userService.postUser(email, password, userName, " ", "", " ", CommonUtils.getDateNow());

        call.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Call<APIResponse<User>> call, Response<APIResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        UserRepository.getInstance().insert(response.body().getData());
                        JointlyApplication.setCurrentSignInUser(response.body().getData());
                        interactor.onSuccess();
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
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

    /**
     * Function that check if the fields introduced are validate
     * @param email
     * @param password
     * @param confirmPassword
     * @param userName
     * @return false if is not valid, true if is valid
     */
    private boolean checkFieldsValid(String email, String password, String confirmPassword, String userName) {
        if(TextUtils.isEmpty(email)) {
            interactor.onEmailEmptyError();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            interactor.onPasswordEmptyError();
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            interactor.onPasswordEmptyError();
            return false;
        }
        if(TextUtils.isEmpty(userName)) {
            interactor.onUserNameEmptyError();
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            interactor.onEmailFormatError();
            return false;
        }
        if (!CommonUtils.isPasswordValid(password)) {
            interactor.onPasswordFormatError();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            interactor.onPasswordNotEqualError();
            return false;
        }
        return true;
    }
}
