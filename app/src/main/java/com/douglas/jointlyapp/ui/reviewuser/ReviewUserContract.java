package com.douglas.jointlyapp.ui.reviewuser;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.ui.base.BasePresenter;

import java.util.List;

/**
 * Interface that set the call logic within view and presenter
 */
public interface ReviewUserContract {

    interface View {
        void onSuccessSendMessage(UserReviewUser userReviewUser);
        void setReviewEmpty();
        void onSuccess(List<UserReviewUser> userReviewUserList);
        void setError(String message);
    }

    interface Presenter extends BasePresenter {
        void loadReview(User user);
        void sendMessage(UserReviewUser userReviewUser);
    }
}
