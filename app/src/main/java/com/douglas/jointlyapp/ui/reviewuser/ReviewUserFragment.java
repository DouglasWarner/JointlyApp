package com.douglas.jointlyapp.ui.reviewuser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that shows the current review on ViewPager2 from UserProfile
 */
public class ReviewUserFragment extends Fragment implements ReviewUserContract.View {

    /**
     * listener that connect this view with his parent
     */
    public interface onSendRating {
        void updateRating();
    }

    //region Variables

    private User user;

    private TextInputLayout tilSendMessage;
    private TextInputEditText tieSendMessage;
    private RatingBar rbStars;
    private ImageButton imgBtnSendMessage;
    private TextView tvNoReviewData;
    private RecyclerView rvReview;
    private ReviewAdapter reviewAdapter;
    private View flSendMessage;

    private onSendRating onSendRating;
    private ReviewUserContract.Presenter presenter;

    //endregion

    public ReviewUserFragment(User user, onSendRating onSendRating) {
        this.user = user;
        this.onSendRating = onSendRating;
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
        setListener();

        presenter = new ReviewUserPresenter(this);
    }

    /**
     * initUI
     * @param view
     */
    private void initUI(@NonNull View view) {
        tvNoReviewData = view.findViewById(R.id.tvNoReviewData);
        rvReview = view.findViewById(R.id.rvReview);
        tilSendMessage = view.findViewById(R.id.tilSendMessage);
        tieSendMessage =view.findViewById(R.id.tieSendMessage);
        imgBtnSendMessage = view.findViewById(R.id.imgBtnSendReview);
        rbStars = view.findViewById(R.id.rbStars);
        flSendMessage = view.findViewById(R.id.flSendMessage);

        // set visibility
        if(JointlyPreferences.getInstance().getUser().equals(user.getEmail())) {
            flSendMessage.setVisibility(View.GONE);
        }
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

    /**
     * setListeners
     */
    private void setListener() {
        tilSendMessage.setEndIconOnClickListener(v -> {
            if(user != null) {
                String loginUser = JointlyPreferences.getInstance().getUser();
                UserReviewUser userReviewUser = new UserReviewUser(loginUser, user.getEmail(), CommonUtils.getDateNow(), tieSendMessage.getText().toString(),
                        (int)rbStars.getRating());
                presenter.sendMessage(userReviewUser);
            }
        });
    }

    /**
     * load all the review of the current user
     */
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
        onSendRating.updateRating();
        Toast.makeText(getContext(), getString(R.string.success_send_review), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setError(String message) {
        Snackbar.make(getView(), message != null ? message : getString(R.string.default_error_action), Snackbar.LENGTH_SHORT);
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