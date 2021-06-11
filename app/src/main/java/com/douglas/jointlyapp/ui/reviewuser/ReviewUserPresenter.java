package com.douglas.jointlyapp.ui.reviewuser;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserReviewUser;

import java.util.List;

public class ReviewUserPresenter implements ReviewUserInteractorImpl.ReviewInteractor, ReviewUserContract.Presenter {

    private ReviewUserContract.View view;
    private ReviewUserInteractorImpl interactor;

    public ReviewUserPresenter(ReviewUserContract.View view) {
        this.view = view;
        interactor = new ReviewUserInteractorImpl(this);
    }

    @Override
    public void loadReview(User user) {
        interactor.loadReview(user);
    }

    @Override
    public void onReviewEmpty() {
        view.setReviewEmpty();
    }

    @Override
    public void onSuccess(List<UserReviewUser> userReviewUserList) {
        view.onSuccess(userReviewUserList);
    }

    @Override
    public void onError(String message) {
        view.setError(message);
    }

    @Override
    public void onDestroy() {
        this.interactor = null;
        this.view = null;
    }
}
