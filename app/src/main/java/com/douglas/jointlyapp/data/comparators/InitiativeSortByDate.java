package com.douglas.jointlyapp.data.comparators;

import com.douglas.jointlyapp.data.model.Initiative;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class InitiativeSortByDate implements Comparator<Initiative>{
    @Override
    public int compare(Initiative o1, Initiative o2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int result = 0;
        try {
            result = simpleDateFormat.parse(o1.getTarget_date()).compareTo(simpleDateFormat.parse(o2.getTarget_date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }
}
