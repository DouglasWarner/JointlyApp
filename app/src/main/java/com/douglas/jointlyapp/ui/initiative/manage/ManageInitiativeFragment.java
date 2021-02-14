package com.douglas.jointlyapp.ui.initiative.manage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.dialog.DatePickerFragment;
import com.douglas.jointlyapp.ui.dialog.TimePickerFragment;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ManageInitiativeFragment extends Fragment implements ManageInitiativeContract.View {

    //TODO Implementar MVP de esta clase

    private TextInputLayout tilName;
    private TextInputLayout tilDescription;
    private TextInputLayout tilLocation;
    private TextInputLayout tilTargetArea;
    private TextInputLayout tilTargetDate;
    private TextInputLayout tilTargetTime;
    private TextInputLayout tilTargetAmount;
    private TextInputEditText tieName;
    private TextInputEditText tieDescription;
    private TextInputEditText tieLocation;
    private TextInputEditText tieTargetArea;
    private TextInputEditText tieTargetDate;
    private TextInputEditText tieTargetTime;
    private TextInputEditText tieTargetAmount;

    private Initiative initiative;
    private boolean isEditing;

    private ManageInitiativePresenter presenter;

    private FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_initiative, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        initiative = (Initiative) bundle.getSerializable("initiative");

        if(initiative == null)
            isEditing = false;
        else
            isEditing = true;

        tilName = view.findViewById(R.id.tilTitle);
        tilDescription = view.findViewById(R.id.tilDescription);
        tilLocation = view.findViewById(R.id.tilLocation);
        tilTargetArea = view.findViewById(R.id.tilTargetArea);
        tilTargetDate = view.findViewById(R.id.tilTargetDate);
        tilTargetTime = view.findViewById(R.id.tilTargetTime);
        tilTargetAmount = view.findViewById(R.id.tilTargetAmount);

        tieName = view.findViewById(R.id.tieNameInitiative);
        tieDescription = view.findViewById(R.id.tieDescription);
        tieLocation = view.findViewById(R.id.tieLocation);
        tieTargetArea = view.findViewById(R.id.tieTargetArea);
        tieTargetDate = view.findViewById(R.id.tieTargetDate);
        tieTargetTime = view.findViewById(R.id.tieTargetTime);
        tieTargetAmount = view.findViewById(R.id.tieTargetAmount);

        if(isEditing)
        {
            tieName.setText(initiative.getName());
            tieDescription.setText(initiative.getDescription());
            tieLocation.setText(initiative.getLocation());
            tieTargetArea.setText(initiative.getTargetArea());
            tieTargetDate.setText(initiative.getTargetDate());
            tieTargetTime.setText(initiative.getTargetTime());
            tieTargetAmount.setText(initiative.getTargetAmount());
        }

        tieTargetDate.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        tieTargetTime.setOnClickListener(v -> {
            showTimePickerDialog();
        });

        floatingActionButton = getActivity().findViewById(R.id.faButton);

        if(isEditing)
            manageTextInputEditTextEnable();

        manageFloatingButton();

        presenter = new ManageInitiativePresenter(this);
    }

    private void manageTextInputEditTextEnable() {
        tieName.setEnabled(false);
        tilName.setHelperTextEnabled(false);
        tieTargetAmount.setEnabled(false);
        tilTargetAmount.setHelperTextEnabled(false);
    }

    private void manageFloatingButton() {
        if(isEditing)
        {
            floatingActionButton.setImageResource(R.drawable.ic_edit);
            floatingActionButton.setOnClickListener(v -> {
                editInitiative();
            });
        }
        else
        {
            floatingActionButton.setVisibility(View.GONE);
        }
    }

    private void editInitiative() {
        presenter.editInitiative(initiative.getId(), initiative.getName(), initiative.getCreatedAt(), tieTargetDate.getText().toString(),
                tieTargetTime.getText().toString(), tieDescription.getText().toString(), tieTargetArea.getText().toString(), tieLocation.getText().toString(),
                initiative.getImagen(), initiative.getTargetAmount(), initiative.getStatus(), initiative.getCreatedBy());

    }

    private void addInitiative()
    {
        SimpleDateFormat localDateTime = new SimpleDateFormat("hh/MM/yyyy");
        Date now = Calendar.getInstance(TimeZone.getTimeZone("UTF")).getTime();

        Uri imageUri = CommonUtils.getImagenInitiativeDefault(getContext());

        presenter.addInitiative(tieName.getText().toString(), localDateTime.format(now), tieTargetDate.getText().toString(),
                tieTargetTime.getText().toString(), tieDescription.getText().toString(), tieTargetArea.getText().toString(), tieLocation.getText().toString(),
                imageUri, tieTargetAmount.getText().toString(), "A", "usuario");
    }

    private void deleteInitiative()
    {
        presenter.delete(initiative);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        if(!isEditing) {
            menu.add(0, 1, 0, "Añadir").setIcon(R.drawable.ic_check).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    addInitiative();
                    return true;
                }
            });
        }
        else
        {
            menu.add(0, 1, 0, "Eliminar").setIcon(R.drawable.ic_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    deleteInitiative();
                    return true;
                }
            });
        }
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

    @Override
    public void setNameEmpty() {
        tilName.setError("Se requiere un nombre");
    }

    @Override
    public void setNameFormatError() {
        tilName.setError("El nombre debe tener entre 5 y 40 caracteres");
    }

    @Override
    public void setLocationEmpty() {
        tilLocation.setError("Se requiere una localidad");
    }

    @Override
    public void setTargetAreaEmpty() {
        tilTargetArea.setError("Se requiere un area objetivo");
    }

    @Override
    public void setTargetDateEmpty() {
        tilTargetDate.setError("Se requiere una fecha");
    }

    @Override
    public void setTargetDateBeforeNowError() {
        tilTargetDate.setError("La fecha debe ser de hoy hacia el futuro");
    }

    @Override
    public void setTargetTimeEmpty() {
        tilTargetTime.setError("Se requiere una hora");
    }

    @Override
    public void setTargetAmountEmpty() {
        tilTargetAmount.setError("Se requiere una cantidad prevista a modo de información");
    }

    @Override
    public void setTargetAmountFormatError() {
        tilTargetAmount.setError("No puede ser 0 o inferior");
    }

    @Override
    public void setCannotDeleted() {
        Toast.makeText(getContext(), "Imposible eleminar porque ya hay usuarios unidos", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void setSuccessDeleted() {
        Toast.makeText(getContext(), "Iniciativa "+initiative.getName()+" eliminada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        if(isEditing)
        {
            Toast.makeText(getContext(), "Iniciativa "+initiative.getName()+" editada", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(), "Iniciativa "+tieName.getText().toString()+" añadida", Toast.LENGTH_SHORT).show();
        }


        getActivity().onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}