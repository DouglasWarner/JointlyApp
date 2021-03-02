package com.douglas.jointlyapp.data.comparators;

import android.icu.util.GregorianCalendar;
import android.text.format.DateUtils;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;

public class InitiativeSortByDate implements Comparator<Initiative>{
    @Override
    public int compare(Initiative o1, Initiative o2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int result = 0;
        try {
            result = simpleDateFormat.parse(o1.getTargetDate()).compareTo(simpleDateFormat.parse(o2.getTargetDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }
}
