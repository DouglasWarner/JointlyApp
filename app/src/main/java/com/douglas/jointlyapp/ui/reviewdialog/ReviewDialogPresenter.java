package com.douglas.jointlyapp.ui.reviewdialog;

import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.ui.reviewuser.ReviewUserContract;

public class ReviewDialogPresenter implements ReviewDialogInteractorImpl.ReviewInteractor, ReviewDialogContract.Presenter {

    private ReviewDialogContract.View view;
    private ReviewDialogInteractorImpl interactor;

    public ReviewDialogPresenter(ReviewDialogContract.View view) {
        this.view = view;
        interactor = new ReviewDialogInteractorImpl(this);
    }

    @Override
    public void sendMessage(UserReviewUser userReviewUser) {
        interactor.sendReview(userReviewUser);
    }

    @Override
    public void onSuccessSendMessage(UserReviewUser userReviewUser) {
        view.onSuccessSendMessage(userReviewUser);
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
