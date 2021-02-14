package com.douglas.jointlyapp.ui.initiative.manage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.douglas.jointlyapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditInitiativeFragment extends Fragment {

    private FloatingActionButton floatingActionButton;

    private TextInputLayout tilTitle;
    private TextInputLayout tilDescription;
    private TextInputLayout tilLocation;
    private TextInputLayout tilTargetArea;
    private TextInputLayout tilTargetDate;
    private TextInputLayout tilTargetTime;
    private TextInputLayout tilTargetAmount;
    private TextInputEditText tieTitle;
    private TextInputEditText tieDescription;
    private TextInputEditText tieLocation;
    private TextInputEditText tieTargetArea;
    private TextInputEditText tieTargetDate;
    private TextInputEditText tieTargetTime;
    private TextInputEditText tieTargetAmount;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_initiative, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingActionButton = getActivity().findViewById(R.id.faButton);

        tilTitle = view.findViewById(R.id.tilTitle);
        tilDescription = view.findViewById(R.id.tilDescription);
        tilLocation = view.findViewById(R.id.tilLocation);
        tilTargetArea = view.findViewById(R.id.tilTargetArea);
        tilTargetDate = view.findViewById(R.id.tilTargetDate);
        tilTargetTime = view.findViewById(R.id.tilTargetTime);
        tilTargetAmount = view.findViewById(R.id.tilTargetAmount);

        tieTitle = view.findViewById(R.id.tieNameInitiative);
        tieDescription = view.findViewById(R.id.tieDescription);
        tieLocation = view.findViewById(R.id.tieLocation);
        tieTargetArea = view.findViewById(R.id.tieTargetArea);
        tieTargetDate = view.findViewById(R.id.tieTargetDate);
        tieTargetTime = view.findViewById(R.id.tieTargetTime);
        tieTargetAmount = view.findViewById(R.id.tieTargetAmount);

        manageFloatingButton();
    }

    private void manageFloatingButton() {
        floatingActionButton.setImageResource(R.drawable.ic_edit);
        floatingActionButton.setOnClickListener(v -> {
            editInitiative();
        });
    }

    private void editInitiative() {
        //TODO navegar hacia initiative
        getActivity().onBackPressed();
    }
}