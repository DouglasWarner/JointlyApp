package com.douglas.jointlyapp.ui.showinitiative;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.adapter.UserJoinedAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

//TODO implementar el MVP
public class ShowInitiativeFragment extends Fragment implements UserJoinedAdapter.ManageInitiative {

    private ImageView imgInitiative;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvTargetArea;
    private TextView tvTargetDate;
    private TextView tvTargetTime;
    private TextView tvTargetAmount;
    private ImageView imgUserCreator;
    private User user;
    private Initiative initiative;

    private RecyclerView rvUserJoined;
    private UserJoinedAdapter adapter;

    private View viewBottomSheet;
    private BottomSheetBehavior<View> bottomSheetBehavior;

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

        showBottomSheetBehavior();

        Bundle bundle = getArguments();
        initiative = (Initiative) bundle.getSerializable("initiative");

        imgInitiative = view.findViewById(R.id.imgInitiative);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvLocation = view.findViewById(R.id.tvUserLocation);
        tvTargetArea = view.findViewById(R.id.tvTargetArea);
        tvTargetDate = view.findViewById(R.id.tvTargetDate);
        tvTargetTime = view.findViewById(R.id.tvTargetTime);
        tvTargetAmount = view.findViewById(R.id.tvTargetAmount);

        imgInitiative.setImageResource(R.drawable.playasucia);
        tvTitle.setText(initiative.getName());
        tvDescription.setText(initiative.getDescription());
        tvLocation.setText(initiative.getLocation());
        tvTargetArea.setText(initiative.getTargetArea());
        tvTargetDate.setText(initiative.getTargetDate());
        tvTargetTime.setText(initiative.getTargetTime());
        tvTargetAmount.setText(initiative.getTargetAmount());

        rvUserJoined = view.findViewById(R.id.rvUserJoined);

        adapter = new UserJoinedAdapter(new ArrayList<>(), this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rvUserJoined.setLayoutManager(layoutManager);
        rvUserJoined.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void showBottomSheetBehavior() {
        viewBottomSheet = getActivity().findViewById(R.id.bottomSheetJoinInitiative);
        bottomSheetBehavior = BottomSheetBehavior.from(viewBottomSheet);

        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void hideBottomSheetBehavior() {
        viewBottomSheet = getActivity().findViewById(R.id.bottomSheetJoinInitiative);
        bottomSheetBehavior = BottomSheetBehavior.from(viewBottomSheet);
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideBottomSheetBehavior();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideBottomSheetBehavior();
    }

    @Override
    public void onClick(View UserView) {
        User user = adapter.getUserItem(rvUserJoined.getChildAdapterPosition(UserView));

        Toast.makeText(getContext(), "Usuario: " + user.getName(), Toast.LENGTH_SHORT).show();
    }
}