package com.douglas.jointlyapp.ui.initiative.manage;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Countries;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.TargetArea;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.dialog.DatePickerFragment;
import com.douglas.jointlyapp.ui.dialog.TimePickerFragment;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
    private AutoCompleteTextView tieLocation;
    private AutoCompleteTextView tieTargetArea;
    private TextInputEditText tieTargetDate;
    private TextInputEditText tieTargetTime;
    private TextInputEditText tieTargetAmount;

    private ArrayAdapter targetAreaAdapter;
    private List<String> targetAreas;

    private ArrayAdapter countriesAdapter;
    private List<String> autoCompleteCountries;

    private Initiative initiative;
    private Bitmap imageInitiative;
    private boolean isEditing;

    private ManageInitiativePresenter presenter;

    private View coordinatorLayout;

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

        initiative = bundle != null ? (Initiative) bundle.getSerializable(Initiative.TAG) : null;

        isEditing = initiative != null;

        initUI(view);
        setOnClickUI();

        if(isEditing) {
            setInitiative(initiative);
            setTextInputEditTextNoEnable();
        }

        presenter = new ManageInitiativePresenter(this);
    }

    /**
     *
     * @param view
     */
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

        coordinatorLayout = getActivity().findViewById(R.id.coordinator_main);
    }

    /**
     * setOnClickUI
     */
    private void setOnClickUI() {
        imgBtnImagen.setOnClickListener(v -> {
            if(ActivityCompat.checkSelfPermission(JointlyApplication.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
            else {
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
        if(requestCode == JointlyApplication.REQUEST_PERMISSION_CODE) {
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(getActivity(), "Se necesitan los permisos para abrir la galeria", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * setInitiative
     * @param init
     */
    private void setInitiative(Initiative init) {
        //TODO mirar porque me da exception de tamaño de memoria
        init.setImagen(init.getImagen() != null ? init.getImagen() : CommonUtils.getImagenInitiativeDefault(JointlyApplication.getContext()));

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

    /**
     * setTextInputEditTextNoEnable for editing mode
     */
    private void setTextInputEditTextNoEnable() {
        tieName.setEnabled(false);
        tilName.setHelperTextEnabled(false);
        tieTargetAmount.setEnabled(false);
        tilTargetAmount.setHelperTextEnabled(false);
    }

    //TODO quizas obtener el usuario de firebase
    /**
     * editInitiative
     */
    private void editInitiative() {
        String created_by = JointlyPreferences.getInstance().getUser();

        presenter.editInitiative(initiative.getId(), initiative.getName(), initiative.getCreated_at(), tieTargetDate.getText().toString(),
                tieTargetTime.getText().toString(), tieDescription.getText().toString(), tieTargetArea.getText().toString(), tieLocation.getText().toString(),
                initiative.getImagen(), initiative.getTarget_amount(), created_by, initiative.getRef_code());

    }

    /**
     * addInitiative
     */
    private void addInitiative() {
        String user = JointlyPreferences.getInstance().getUser();

        if(imageInitiative == null)
            imageInitiative = CommonUtils.getImagenInitiativeDefault(JointlyApplication.getContext());

        initiative = new Initiative(tieName.getText().toString(), CommonUtils.getDateNow(), String.format("%s %s", tieTargetDate.getText().toString(), tieTargetTime.getText().toString()),
                tieDescription.getText().toString(), tieTargetArea.getText().toString(), tieLocation.getText().toString(),
                imageInitiative, tieTargetAmount.getText().toString(), user);

        presenter.addInitiative(tieName.getText().toString(), tieTargetDate.getText().toString(),
                tieTargetTime.getText().toString(), tieDescription.getText().toString(), tieTargetArea.getText().toString(),
                tieLocation.getText().toString(), imageInitiative, tieTargetAmount.getText().toString(), user);
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

    //TODO exception de memoria view
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

    /**
     * openGallery for select imagen
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, JointlyApplication.REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadCountries();
        presenter.loadTargetArea();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == JointlyApplication.REQUEST_IMAGE_GALLERY) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                Uri imagen = data.getData();
                imgBtnImagen.setImageURI(imagen);
                imageInitiative = ((BitmapDrawable)imgBtnImagen.getDrawable()).getBitmap();
            } else {
                Toast.makeText(getActivity(), "No se ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        menu.add(0, 1, 0, getString(R.string.menu_item_title_add)).setIcon(R.drawable.ic_check).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(0).setOnMenuItemClickListener(item -> {
            if (isEditing) {
                editInitiative();
            } else {
                addInitiative();
            }
            return true;
        });
    }

    @Override
    public void setNameEmpty() {
        tilName.setError(getString(R.string.error_initiative_name_empty));
    }

    @Override
    public void setNameFormatError() {
        tilName.setError(getString(R.string.error_initiative_name_format));
    }

    @Override
    public void setLocationEmpty() {
        tilLocation.setError(getString(R.string.error_initiative_location_empty));
    }

    @Override
    public void setTargetAreaEmpty() {
        tilTargetArea.setError(getString(R.string.error_initiative_area_empty));
    }

    @Override
    public void setTargetDateEmpty() {
        tilTargetDate.setError(getString(R.string.error_initiative_date_empty));
    }

    @Override
    public void setTargetDateBeforeNowError() {
        tilTargetDate.setError(getString(R.string.error_initiative_date_valid));
    }

    @Override
    public void setTargetTimeEmpty() {
        tilTargetTime.setError(getString(R.string.error_initiative_time_empty));
    }

    @Override
    public void setTargetAmountEmpty() {
        tilTargetAmount.setError(getString(R.string.error_initiative_amount_empty));
    }

    @Override
    public void setTargetAmountFormatError() {
        tilTargetAmount.setError(getString(R.string.error_initiative_amount_format));
    }

    //TODO quizar aqui lanzar el notification push para todos los dispositivos
    @Override
    public void onSuccess(Initiative init) {
        if(isEditing) {
            Snackbar.make(coordinatorLayout, String.format(getString(R.string.initiative_success_edited), init.getName()), Snackbar.LENGTH_SHORT).show();
        } else {
            initiative = init;
//            if(JointlyPreferences.getInstance().getNotificationAvaible())
//                Notifications.showNotificationAddInitiative(getActivity(), initiative.getId());
            Snackbar.make(coordinatorLayout, String.format(getString(R.string.initiative_success_created), init.getName()), Snackbar.LENGTH_SHORT).show();
        }

        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onUnsuccess() {
        if(isEditing) {
            Snackbar.make(coordinatorLayout, getString(R.string.error_edit_initiative), Snackbar.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(coordinatorLayout, getString(R.string.error_insert_initiative), Snackbar.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setCountries(List<Countries> countries) {
        autoCompleteCountries = new ArrayList<>();
        autoCompleteCountries.addAll(countries.stream().map(Countries::getName).collect(Collectors.toList()));
        autoCompleteCountries.add("holaa");
        autoCompleteCountries.add("como estas");

        countriesAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, autoCompleteCountries);
        tieLocation.setThreshold(1);
//        tieLocation.setOnClickListener(v -> {
//
//        });
        tieLocation.setAdapter(countriesAdapter);
    }

    @Override
    public void setTargetArea(List<TargetArea> targetArea) {
        targetAreas = new ArrayList<>();
        this.targetAreas.add(getString(R.string.spHintTargetArea));
        this.targetAreas.addAll(targetArea.stream().map(TargetArea::getName).collect(Collectors.toList()));
        this.targetAreas.add("Hollaaaa");
        this.targetAreas.add("caracolaaa");

        targetAreaAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, targetAreas);
        tieTargetArea.setAdapter(targetAreaAdapter);
    }

    @Override
    public void setOnError(String message) {
        Snackbar.make(coordinatorLayout, message != null ? message : getString(R.string.default_error_action), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}