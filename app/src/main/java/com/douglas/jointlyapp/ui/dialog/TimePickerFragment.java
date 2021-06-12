package com.douglas.jointlyapp.ui.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Dialog that display timepicker
 */
public class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;
    private static final TimePickerFragment timePickerFragment;

    static {
        timePickerFragment = new TimePickerFragment();
    }

    public static TimePickerFragment newInstance(TimePickerDialog.OnTimeSetListener listener) {
        timePickerFragment.setListener(listener);
        return timePickerFragment;
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), listener, hour, minute, true);
    }
}
