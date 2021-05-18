package com.douglas.jointlyapp.ui.showinitiative;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.adapter.UserJoinedAdapter;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.service.BackgroundJobService;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

/**
 * La ventada de la iniciativa en particular
 */
public class ShowInitiativeFragment extends Fragment implements ShowInitiativeContract.View, UserJoinedAdapter.ManageInitiative {

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
    private int idInitiative;

    private TextView tvNoUserData;
    private RecyclerView rvUserJoined;
    private UserJoinedAdapter adapter;

    private View viewBottomSheet;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private Button join;
    private ImageButton chat;

    private ShowInitiativeContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        idInitiative = bundle.getInt(Initiative.TAG);

        initUI(view);
        initRecycler();

        setOnClickUI();

        presenter = new ShowInitiativePresenter(this);
    }

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
    }

    private void setInitiative(Initiative init) {
        initiative = init;
        imgInitiative.setImageBitmap(init.getImagen());
        tvTitle.setText(init.getName());
        tvDescription.setText(init.getDescription());
        tvLocation.setText(init.getLocation());
        tvTargetArea.setText(init.getTargetArea());
        tvTargetDate.setText(init.getTargetDate().split(" ")[0]);
        tvTargetTime.setText(init.getTargetDate().split(" ")[1]);
        tvTargetAmount.setText(init.getTargetAmount());
    }

    private void initRecycler() {
        adapter = new UserJoinedAdapter(new ArrayList<>(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rvUserJoined.setLayoutManager(layoutManager);
        rvUserJoined.setAdapter(adapter);
    }

    private void setOnClickUI() {
        imgUserCreator.setOnClickListener(v -> {
            goToUserProfile(initiative.getCreatedBy());
        });
    }

    private void showBottomSheetBehavior() {
        viewBottomSheet = getActivity().findViewById(R.id.bottomSheetJoinInitiative);
        join = viewBottomSheet.findViewById(R.id.btnJoin);
        chat = viewBottomSheet.findViewById(R.id.btnChat);
        bottomSheetBehavior = BottomSheetBehavior.from(viewBottomSheet);

        join.setOnClickListener(v -> {
            if (join.getText().toString().equals("Unirme")) {
                presenter.joinInitiative(initiative);
                Toast.makeText(getContext(), "Se ha unido a la iniciativa", Toast.LENGTH_SHORT).show();
            } else {
                presenter.unJoinInitiative(initiative);
                Toast.makeText(getContext(), "Ha cancelado la participación a la iniciativa", Toast.LENGTH_SHORT).show();
            }
        });

        chat.setOnClickListener(v -> {
            Bundle bundle = new Bundle();

            bundle.putInt(Initiative.TAG, initiative.getId());

            NavHostFragment.findNavController(this).navigate(R.id.action_showInitiativeFragment_to_chatFragment, bundle);
        });

        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initScheludeJob(Initiative initiative) {
        ComponentName serviceComponentName = new ComponentName(getContext(), BackgroundJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponentName);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Initiative.TAG, initiative);
        builder.setTransientExtras(bundle);

        JointlyApplication.jobScheduler = (JobScheduler) getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JointlyApplication.jobScheduler.schedule(builder.build());
    }

    private void cancelScheludeJob()
    {
        if(JointlyApplication.jobScheduler != null)
            JointlyApplication.jobScheduler.cancel(0);
    }

    private void hideBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(viewBottomSheet);
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Si vengo de Mis iniciativas
        if(!getArguments().getBoolean("history"))
            showBottomSheetBehavior();

        presenter.loadInitiative(idInitiative);
        presenter.loadUserStateJoined(idInitiative);
        presenter.loadUserOwner(idInitiative);
        presenter.loadListUserJoined(idInitiative);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideBottomSheetBehavior();
    }

    @Override
    public void onClick(View item) {
        User user = adapter.getUserItem(rvUserJoined.getChildAdapterPosition(item));

        goToUserProfile(user.getEmail());
    }

    private void goToUserProfile(String user)
    {
        Bundle bundle = new Bundle();
        bundle.putString(User.TAG, user);

        if(user.equals(JointlyPreferences.getInstance().getUser()))
            NavHostFragment.findNavController(this).navigate(R.id.action_showInitiativeFragment_to_profileFragment);
        else
            NavHostFragment.findNavController(this).navigate(R.id.action_showInitiativeFragment_to_userProfileFragment, bundle);
    }

    @Override
    public void setJoined() {
//        initScheludeJob(initiative);
        join.setText("Cancelar");
    }

    @Override
    public void setUnJoined() {
//        cancelScheludeJob();
        join.setText("Unirme");
    }

    @Override
    public void setUserListEmpty() {
        tvNoUserData.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLoadListUserJoined(List<User> userList) {
        if(tvNoUserData.getVisibility() == View.VISIBLE)
            tvNoUserData.setVisibility(View.GONE);

        adapter.update(userList);
    }

    @Override
    public void setLoadUserOwner(User user) {
        imgUserCreator.setImageBitmap(user.getImagen());
    }

    @Override
    public void onSuccessLoad(Initiative initiative) {
        setInitiative(initiative);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}