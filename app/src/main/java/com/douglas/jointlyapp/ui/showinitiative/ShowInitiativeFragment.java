package com.douglas.jointlyapp.ui.showinitiative;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.adapter.UserJoinedAdapter;
import com.douglas.jointlyapp.ui.initiative.InitiativeFragment;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.service.BackgroundJobService;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import net.glxn.qrgen.android.QRCode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that represents the initiative display
 */
public class ShowInitiativeFragment extends Fragment implements ShowInitiativeContract.View, UserJoinedAdapter.ManageInitiative {

    //region Variables

    private ImageView imgInitiative;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvTargetArea;
    private TextView tvTargetDate;
    private TextView tvTargetTime;
    private TextView tvTargetAmount;
    private ImageView imgUserCreator;
    private Initiative initiative;
    private User userOwner;

    private TextView tvNoUserData;
    private RecyclerView rvUserJoined;
    private UserJoinedAdapter adapter;

    private View coordinatorLayout;
    private View viewBottomSheet;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private Button btnJoin;
    private ImageButton imgBtnChat;

    private boolean isHistory;
    private boolean isCreated;

    private ShowInitiativeContract.Presenter presenter;

    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_initiative, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        initiative = (Initiative) (bundle != null ? bundle.getSerializable(Initiative.TAG) : null);
        isHistory = bundle != null && bundle.getBoolean(InitiativeFragment.TYPE_HISTORY);
        isCreated = bundle != null && bundle.getBoolean(InitiativeFragment.TYPE_CREATED);

        initUI(view);
        initRecycler();

        // When I come from InitiativeFragment
        if(!isHistory)
            showBottomSheetBehavior();
        if(isCreated) {
            setIsCreated();
        } else {
            imgBtnChat.setImageResource(R.drawable.ic_chat);
            btnJoin.setText(R.string.join_user_join);
            //TODO
//            btnJoin.setPaddingRelative((int) getResources().getDimension(R.dimen.default_dimen_large), 0, 0, 0);
            btnJoin.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }

        setInitiative(initiative);
        setOnClickUI();

        presenter = new ShowInitiativePresenter(this);
    }

    /**
     * setIsCreated
     * set view to display fragment for initiative created by user
     */
    private void setIsCreated() {
        imgBtnChat.setEnabled(true);
        imgBtnChat.setImageResource(R.drawable.ic_qr);
        btnJoin.setText(R.string.show_initiative_go_to_chat);
        btnJoin.setPaddingRelative((int) getResources().getDimension(R.dimen.default_dimen_large), 0, 0, 0);
        btnJoin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_chat,0,0,0);
    }

    /**
     * initUI
     * @param view
     */
    private void initUI(@NonNull View view) {
        imgInitiative = view.findViewById(R.id.imgBtnInitiative);
        imgUserCreator = view.findViewById(R.id.imgUserCreator);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvLocation = view.findViewById(R.id.tvUserLocation);
        tvTargetArea = view.findViewById(R.id.tvTargetArea);
        tvTargetDate = view.findViewById(R.id.tvTargetDate);
        tvTargetTime = view.findViewById(R.id.tvTargetTime);
        tvTargetAmount = view.findViewById(R.id.tvTargetAmount);
        rvUserJoined = view.findViewById(R.id.rvUserJoined);
        tvNoUserData = view.findViewById(R.id.tvNoUserData);

        coordinatorLayout = getActivity().findViewById(R.id.coordinator_main);
        viewBottomSheet = coordinatorLayout.findViewById(R.id.bottomSheetJoinInitiative);
        btnJoin = viewBottomSheet.findViewById(R.id.btnJoin);
        imgBtnChat = viewBottomSheet.findViewById(R.id.btnChat);
        bottomSheetBehavior = BottomSheetBehavior.from(viewBottomSheet);
    }

    /**
     * setInitiative
     * @param init
     */
    private void setInitiative(Initiative init) {
        initiative = init;
        if(init.getImage() != null){
            Glide.with(JointlyApplication.getContext())
                    .setDefaultRequestOptions(CommonUtils.getGlideOptions(Initiative.TAG))
                    .load(Apis.getURLIMAGE()+init.getImage())
                    .into(imgInitiative);
        } else {
            imgInitiative.setImageBitmap(CommonUtils.getImagenInitiativeDefault(JointlyApplication.getContext()));
        }
        tvTitle.setText(init.getName());
        tvDescription.setText(init.getDescription());
        tvLocation.setText(init.getLocation());
        tvTargetArea.setText(init.getTarget_area());
        tvTargetDate.setText(init.getTarget_date().split(" ")[0]);
        tvTargetTime.setText(init.getTarget_date().split(" ")[1]);
        tvTargetAmount.setText(init.getTarget_amount());
    }

    /**
     * initRecycler
     */
    private void initRecycler() {
        adapter = new UserJoinedAdapter(new ArrayList<>(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rvUserJoined.setLayoutManager(layoutManager);
        rvUserJoined.setAdapter(adapter);
    }

    /**
     * setOnClickUI
     */
    private void setOnClickUI() {
        imgUserCreator.setOnClickListener(v -> {
            goToUserProfile(userOwner);
        });

        btnJoin.setOnClickListener(v -> {
            if(isCreated) {
                goToChatFragment();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.dialog_title_join_initiative)
                        .setMessage(R.string.dialog_alert_info_message_join_initiative)
                        .setIcon(R.drawable.ic_info)
                        .setPositiveButton(R.string.dialog_positive_btn_join_initiative, (dialog1, which) -> {
                            presenter.joinInitiative(initiative);
                            dialog1.dismiss();
                        })
                        .setNegativeButton(R.string.dialog_negative_btn_join_initiative, (dialog1, which) -> {
                            dialog1.dismiss();
                        });

                dialog.show();
            }
        });

        imgBtnChat.setOnClickListener(v -> {
            // isCreated -> ShowQRGenerator
            if(isCreated) {
                View view = getViewQRGenerated();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.dialog_title_qr_initiative)
                        .setView(view)
                        .setNeutralButton(R.string.dialog_neutral_qr, (dialog1, which) -> {
                            dialog1.dismiss();
                        });
                dialog.show();
            } else {
                // isJoined -> ShowChatInitiative
                goToChatFragment();
            }
        });
    }

    /**
     * Init view for display qr generated
     * @return
     */
    @NotNull
    private View getViewQRGenerated() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_qr, null, false);
        ImageView qr = view.findViewById(R.id.ivQR);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels/2;
        int width = displayMetrics.widthPixels/2;
        qr.setImageBitmap(QRCode.from(initiative.getRef_code()).withSize(width, height).bitmap());
        return view;
    }

    /**
     * goToChatFragment
     */
    private void goToChatFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Initiative.TAG, initiative);
        NavHostFragment.findNavController(this).navigate(R.id.action_showInitiativeFragment_to_chatFragment, bundle);
    }

    /**
     * showBottomSheetBehavior
     */
    private void showBottomSheetBehavior() {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    /**
     * hideBottomSheetBehavior
     */
    private void hideBottomSheetBehavior() {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    //TODO no se que es esto, quizas para lo de notificaciones de chat
    private void initScheludeJob(Initiative initiative) {
        ComponentName serviceComponentName = new ComponentName(getContext(), BackgroundJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponentName);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Initiative.TAG, initiative);
        builder.setTransientExtras(bundle);

        JointlyApplication.jobScheduler = (JobScheduler) getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JointlyApplication.jobScheduler.schedule(builder.build());
    }
    private void cancelScheludeJob() {
        if(JointlyApplication.jobScheduler != null)
            JointlyApplication.jobScheduler.cancel(0);
    }

    @Override
    public void onStart() {
        super.onStart();

        //TODO Quizar obtener desde firebase
        String email = JointlyPreferences.getInstance().getUser();

        if(email != null) {
            presenter.loadInitiative(initiative.getId());
            presenter.loadUserOwner(initiative.getCreated_by());
            presenter.loadListUserJoined(initiative.getId());
            if(!isCreated) {
                presenter.loadUserStateJoined(email, initiative.getId());
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        if(isCreated) {
            menu.add(0, 1, 0, getString(R.string.menu_item_title_delete)).setIcon(R.drawable.ic_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, 1, 1, getString(R.string.menu_item_title_edit)).setIcon(R.drawable.ic_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.getItem(0).setOnMenuItemClickListener(item -> {
                deleteInitiative();
                return true;
            });
            menu.getItem(1).setOnMenuItemClickListener(item -> {
                goToEditInitiative();
                return true;
            });
        }
    }

    @Override
    public void onClick(View item) {
        User user = adapter.getUserItem(rvUserJoined.getChildAdapterPosition(item));

        goToUserProfile(user);
    }

    /**
     *
     * @param user
     */
    private void goToUserProfile(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(User.TAG, user);

        //TODO Quizas obtener desde firebase
        if(user.getEmail().equals(JointlyPreferences.getInstance().getUser()))
            NavHostFragment.findNavController(this).navigate(R.id.action_showInitiativeFragment_to_profileFragment);
        else
            NavHostFragment.findNavController(this).navigate(R.id.action_showInitiativeFragment_to_userProfileFragment, bundle);
    }

    /**
     * goToEditInitiative
     */
    private void goToEditInitiative() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Initiative.TAG, initiative);
        NavHostFragment.findNavController(this).navigate(R.id.action_showInitiativeFragment_to_manageInitiativeFragment, bundle);
    }

    /**
     * deleteInitiative
     */
    private void deleteInitiative() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_title_delete_initiative)
                .setMessage(getString(R.string.dialog_message_delete_initiative) + initiative.getName())
                .setIcon(R.drawable.ic_alert)
                .setPositiveButton(R.string.dialog_positive_btn_delete_initiative, (dialog1, which) -> {
                    presenter.delete(initiative);
                    dialog1.dismiss();
                })
                .setNegativeButton(R.string.dialog_negative_btn_delete_initiative, (dialog1, which) -> {
                    dialog1.dismiss();
                });

        dialog.show();
    }

    @Override
    public void setJoined() {
        if(!isCreated) {
            btnJoin.setText(R.string.undo_user_join);
            imgBtnChat.setEnabled(true);
        }
    }

    @Override
    public void setUnJoined() {
        if(!isCreated) {
            btnJoin.setText(R.string.join_user_join);
            imgBtnChat.setEnabled(true);
        }
    }

    @Override
    public void setUserListEmpty() {
        tvNoUserData.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLoadUserStateJoined(boolean joined) {
        if(!isCreated) {
            imgBtnChat.setEnabled(joined);
            btnJoin.setText((joined) ? R.string.undo_user_join : R.string.join_user_join);
        }
    }

    @Override
    public void setLoadListUserJoined(List<User> userList) {
        if(tvNoUserData.getVisibility() == View.VISIBLE)
            tvNoUserData.setVisibility(View.GONE);

        adapter.update(userList);
    }

    @Override
    public void setLoadUserOwner(User user) {
        this.userOwner = user;
        if(user.getImagen() != null) {
            Glide.with(JointlyApplication.getContext())
                    .setDefaultRequestOptions(CommonUtils.getGlideOptions(User.TAG))
                    .load(Apis.getURLIMAGE()+user.getImagen())
                    .into(imgUserCreator);
        } else {
            imgUserCreator.setImageBitmap(CommonUtils.getImagenUserDefault(JointlyApplication.getContext()));
        }
    }

    @Override
    public void setCannotDeleted() {
        Snackbar.make(getView(), getString(R.string.error_initiative_user_joined), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setSuccessDeleted() {
        Snackbar.make(getView(), String.format(getString(R.string.initiative_success_deleted), initiative.getName()), Snackbar.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onError(String message) {
        Snackbar.make(getView(), message != null ? message : getString(R.string.default_error_action), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadInitiative(Initiative initiative) {
        setInitiative(initiative);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        new Thread(this::hideBottomSheetBehavior).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}