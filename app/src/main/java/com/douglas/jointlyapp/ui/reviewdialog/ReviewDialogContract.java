package com.douglas.jointlyapp.ui.reviewdialog;

import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.ui.base.BasePresenter;

public interface ReviewDialogContract {

    interface View {
        void onSuccessSendMessage(UserReviewUser userReviewUser);
        void setError(String message);
    }

    interface Presenter extends BasePresenter {
        void sendMessage(UserReviewUser userReviewUser);
    }
}
