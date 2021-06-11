package com.douglas.jointlyapp.ui.reviewuser;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.ui.base.BasePresenter;

import java.util.List;

public interface ReviewUserContract {

    interface View {
        void setReviewEmpty();
        void onSuccess(List<UserReviewUser> userReviewUserList);
        void setError(String message);
    }

    interface Presenter extends BasePresenter {
        void loadReview(User user);
    }
}
