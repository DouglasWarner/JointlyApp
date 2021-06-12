package com.douglas.jointlyapp.ui.profile;

import android.net.Uri;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BasePresenter;

/**
 * Interface that set the call logic within view and presenter
 */
public interface ProfileContract {

    interface View {
        void onLoadUser(User user);
        void setRatingUser(float average);
        void setUpdateImage();
        void onError(String message);
    }

    interface Presenter extends BasePresenter {
        void loadUser(String userEmail);
        void loadRatingUser(String user);
        void updateImage(User user, Uri image);
    }
}
