package com.douglas.jointlyapp.ui.reviewuser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.ui.adapter.ReviewAdapter;
import com.douglas.jointlyapp.ui.reviewdialog.ReviewDialogContract;
import com.douglas.jointlyapp.ui.reviewdialog.ReviewDialogPresenter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ReviewUserFragment extends Fragment implements ReviewUserContract.View, ReviewDialogContract.View {

    //region Variables

    private User user;
    private TextView tvNoReviewData;
    private RecyclerView rvReview;
    private ReviewAdapter reviewAdapter;

    private ReviewUserContract.Presenter presenter;
    private ReviewDialogContract.Presenter presenterDialog;

    //endregion


    public ReviewUserFragment(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        initRecycler();

        presenter = new ReviewUserPresenter(this);
    }

    /**
     * initUI
     * @param view
     */
    private void initUI(@NonNull View view) {
        tvNoReviewData = view.findViewById(R.id.tvNoReviewData);
        rvReview = view.findViewById(R.id.rvReview);
    }

    /**
     * initRecycler
     */
    private void initRecycler() {
        reviewAdapter = new ReviewAdapter(new ArrayList<>());
        RecyclerView.LayoutManager lmReview = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvReview.setLayoutManager(lmReview);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvReview.getContext(), RecyclerView.VERTICAL);
        rvReview.addItemDecoration(dividerItemDecoration);
        rvReview.setAdapter(reviewAdapter);
    }

    private void initListener() {

    }

    //TODO probar
    public void load() {
        presenter.loadReview(user);
    }

    @Override
    public void setReviewEmpty() {
        tvNoReviewData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(List<UserReviewUser> userReviewUserList) {
        if(tvNoReviewData.getVisibility()== View.VISIBLE){
            tvNoReviewData.setVisibility(View.GONE);
        }

        reviewAdapter.update(userReviewUserList);
    }

    @Override
    public void onSuccessSendMessage(UserReviewUser userReviewUser) {
        reviewAdapter.addMessage(userReviewUser);
    }

    @Override
    public void setError(String message) {
        Snackbar.make(getActivity().findViewById(R.id.coordinator_main), message != null ? message : getString(R.string.default_error_action), Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadReview(user);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}