package com.douglas.jointlyapp.ui.reviewdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

/**
 * ReviewBottomSheetDialog shows SheetBottom for write a Review of User from UserProfile
 */
public class ReviewBottomSheetDialog extends BottomSheetDialogFragment implements ReviewDialogContract.View{

    //region Variables

    private EditText etSendMessage;
    private RatingBar rbStars;
    private ImageButton imgBtnSendMessage;
    private User user;
    private ImageButton imgBtnReviewBack;

    private ReviewDialogContract.Presenter presenter;

    //endregion

    public ReviewBottomSheetDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_review, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        user = bundle != null ? (User) bundle.getSerializable(User.TAG) : null;

        initUI(view);
        setListeners();

        presenter = new ReviewDialogPresenter(this);
    }

    /**
     * setListeners
     */
    private void setListeners() {
        imgBtnSendMessage.setOnClickListener(v -> {
            if(user != null) {
                String loginUser = JointlyPreferences.getInstance().getUser();
                UserReviewUser userReviewUser = new UserReviewUser(loginUser, user.getEmail(), CommonUtils.getDateNow(), etSendMessage.getText().toString(),
                        (int)rbStars.getRating());
                presenter.sendMessage(userReviewUser);
            }
        });

        imgBtnReviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * initUI
     * @param view
     */
    private void initUI(@NonNull View view) {
        etSendMessage =view.findViewById(R.id.etSendMessage);
        imgBtnSendMessage = view.findViewById(R.id.imgBtnSendReview);
        rbStars = view.findViewById(R.id.rbStars);
        imgBtnReviewBack = view.findViewById(R.id.imgBtnReviewBack);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog= (BottomSheetDialog)super.onCreateDialog(savedInstanceState);

        bottomSheetDialog.getBehavior().setDraggable(false);
        bottomSheetDialog.getBehavior().setPeekHeight(0);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onSuccessSendMessage(UserReviewUser userReviewUser) {
        Toast.makeText(getContext(), getString(R.string.success_send_review), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setError(String message) {
        Snackbar.make(getActivity().findViewById(R.id.coordinator_main), message != null ? message : getString(R.string.default_error_action), Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
