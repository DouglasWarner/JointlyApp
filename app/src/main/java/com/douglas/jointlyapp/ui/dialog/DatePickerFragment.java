package com.douglas.jointlyapp.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Dialog that display datepicker
 */
public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private static final DatePickerFragment datePickerFragment;

    static {
        datePickerFragment = new DatePickerFragment();
    }

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        datePickerFragment.setListener(listener);
        return datePickerFragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH)+1;

        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}
