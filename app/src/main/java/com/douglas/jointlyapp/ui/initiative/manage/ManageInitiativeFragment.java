package com.douglas.jointlyapp.ui.initiative.manage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.TimeZoneFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.Notifications;
import com.douglas.jointlyapp.ui.dialog.DatePickerFragment;
import com.douglas.jointlyapp.ui.dialog.TimePickerFragment;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;

public class ManageInitiativeFragment extends Fragment implements ManageInitiativeContract.View {

    //region Variables
    private ImageButton imgBtnImagen;
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
    private Bitmap imageInitiative;
    private boolean isEditing;

    private ManageInitiativePresenter presenter;

    private FloatingActionButton floatingActionButton;
    //endregion

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

        initiative = (Initiative) bundle.getSerializable(Initiative.TAG);

        isEditing = initiative != null;

        initUI(view);
        setOnClickUI();

        manageFloatingButton();

        presenter = new ManageInitiativePresenter(this);

        if(isEditing)
            setInitiative(initiative);
    }

    private void initUI(@NonNull View view) {
        imgBtnImagen = view.findViewById(R.id.imgBtnInitiative);
        tilName = view.findViewById(R.id.tilNameInitiative);
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

        floatingActionButton = getActivity().findViewById(R.id.faButton);
    }

    private void setOnClickUI() {
        imgBtnImagen.setOnClickListener(v -> {
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                openGallery();
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, JointlyApplication.REQUEST_PERMISSION_CODE);
            }
        });

        tieTargetDate.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        tieTargetTime.setOnClickListener(v -> {
            showTimePickerDialog();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == JointlyApplication.REQUEST_PERMISSION_CODE)
        {
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                openGallery();
            }
            else
            {
                Toast.makeText(getActivity(), "Se necesitan los permisos para abrir la galeria", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setInitiative(Initiative init) {
        //TODO mirar porque me da exception de tamaño de memoria
        init.setImagen(init.getImagen() != null ? init.getImagen() : CommonUtils.getImagenInitiativeDefault(getContext()));

        imgBtnImagen.setImageBitmap(init.getImagen());
        tieName.setText(init.getName());
        tieDescription.setText(init.getDescription());
        tieLocation.setText(init.getLocation());
        tieTargetArea.setText(init.getTarget_area());
        tieTargetDate.setText(init.getTarget_date().split(" ")[0]);
        tieTargetTime.setText(init.getTarget_date().split(" ")[1]);
        tieTargetAmount.setText(init.getTarget_amount());

        initiative = init;
    }

    private void setTextInputEditTextNoEnable() {
        tieName.setEnabled(false);
        tilName.setHelperTextEnabled(false);
        tieTargetAmount.setEnabled(false);
        tilTargetAmount.setHelperTextEnabled(false);
    }

    private void manageFloatingButton() {
        if(isEditing)
        {
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

        String user = JointlyPreferences.getInstance().getUser();

        presenter.editInitiative(initiative.getId(), initiative.getName(), initiative.getCreated_at(), tieTargetDate.getText().toString(),
                tieTargetTime.getText().toString(), tieDescription.getText().toString(), tieTargetArea.getText().toString(), tieLocation.getText().toString(),
                initiative.getImagen(), initiative.getTarget_amount(), initiative.getStatus(), user, initiative.getRef_code());

    }

    private void addInitiative()
    {
        String user = JointlyPreferences.getInstance().getUser();

        if(imageInitiative == null)
                imageInitiative = CommonUtils.getImagenInitiativeDefault(getActivity());

        initiative = new Initiative(tieName.getText().toString(), CommonUtils.getDateNow(), String.format("%s %s", tieTargetDate.getText().toString(), tieTargetTime.getText().toString()), tieDescription.getText().toString(), tieTargetArea.getText().toString(), tieLocation.getText().toString(),
                imageInitiative, tieTargetAmount.getText().toString(), "A", user, "");

        presenter.addInitiative(tieName.getText().toString(), tieTargetDate.getText().toString(),
                tieTargetTime.getText().toString(), tieDescription.getText().toString(), tieTargetArea.getText().toString(), tieLocation.getText().toString(),
                imageInitiative, tieTargetAmount.getText().toString(), "A", user);
    }

    private void deleteInitiative()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Eliminando iniciativa")
                .setMessage("Eliminar iniciativa: " + initiative.getName())
                .setIcon(R.drawable.ic_alert)
                .setPositiveButton("Aceptar", (dialog1, which) -> {
                    presenter.delete(initiative);
                })
                .setNegativeButton("Cancelar", (dialog1, which) -> {
                    dialog1.dismiss();
                });

        dialog.show();
    }

    /**
     * Muestra un dialog para seleccionar la hora
     */
    private void showTimePickerDialog() {
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hour = String.format(Locale.getDefault(), "%d:%d",hourOfDay, minute);
                tieTargetTime.setText(hour);
            }
        });

        timePickerFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }


//    public static StoreDetailFragment newInstance(Store store) {
//        StoreDetailFragment fragment = new StoreDetailFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(IntentUtils.EXTRA_STORE, store); // the value of EXTRA_STORE is "_store"
//        // ...
//        fragment.setArguments(args);
//        return fragment;
//    }

    /**
     * Muestra un dialog para seleccionar la fecha
     */
    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat format = new SimpleDateFormat(JointlyApplication.DATEFORMAT2, Locale.getDefault());
                String date = format.format(new GregorianCalendar(year, month, dayOfMonth).getTime());
                tieTargetDate.setText(date);
            }
        });

        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, JointlyApplication.REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == JointlyApplication.REQUEST_IMAGE_GALLERY)
        {
            if(resultCode == Activity.RESULT_OK && data != null)
            {
                Uri imagen = data.getData();
                imgBtnImagen.setImageURI(imagen);
                imageInitiative = ((BitmapDrawable)imgBtnImagen.getDrawable()).getBitmap();
            }
            else
            {
                Toast.makeText(getActivity(), "No se ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        } else {
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
        Toast.makeText(getContext(), "Imposible eliminar porque ya hay usuarios unidos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSuccessDeleted() {
        Toast.makeText(getContext(), "Iniciativa "+initiative.getName()+" eliminada", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onSuccessLoad(Initiative initiative) {
        setInitiative(initiative);
        setTextInputEditTextNoEnable();
    }

    @Override
    public void onSuccess(Initiative init) {
        if(isEditing) {
            Snackbar.make(getView(), "Iniciativa "+init.getName()+" editada", Snackbar.LENGTH_SHORT).show();
//            Toast.makeText(getContext(), "Iniciativa "+init.getName()+" editada", Toast.LENGTH_SHORT).show();
        } else {
            initiative = init;
//            if(JointlyPreferences.getInstance().getNotificationAvaible())
//                Notifications.showNotificationAddInitiative(getActivity(), initiative.getId());
            Snackbar.make(getView(), "Iniciativa "+init.getName()+" añadida", Snackbar.LENGTH_SHORT).show();
//            Toast.makeText(getContext(), "Iniciativa "+init.getName()+" añadida", Toast.LENGTH_SHORT).show();
        }

        NavHostFragment.findNavController(this).popBackStack();
//        getActivity().onBackPressed();
    }

    @Override
    public void onUnsuccess(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setOnError(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}