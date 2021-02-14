package com.douglas.jointlyapp.ui.initiative.manage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.ui.dialog.DatePickerFragment;
import com.douglas.jointlyapp.ui.dialog.TimePickerFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddInitiativeFragment extends Fragment {

    //TODO Implementar MVP de esta clase

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

    private FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_initiative, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        tieTargetDate.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        tieTargetTime.setOnClickListener(v -> {
            showTimePickerDialog();
        });

        floatingActionButton = getActivity().findViewById(R.id.faButton);

        manageFloatingButton();
    }

    private void manageFloatingButton() {
        floatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        menu.add(0,1,0,"Añadir").setIcon(R.drawable.ic_check).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //TODO añadir la iniciativa
                getActivity().onBackPressed();
                return true;
            }
        });
    }

    private void showTimePickerDialog() {
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hour = String.format("%d:%d",hourOfDay, minute);
                tieTargetTime.setText(hour);
            }
        });

        timePickerFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = String.format("%d/%d/%d", dayOfMonth, month+1, year);
                tieTargetDate.setText(date);
            }
        });

        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
}