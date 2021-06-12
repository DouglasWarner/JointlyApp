package com.douglas.jointlyapp.data.comparators;

import com.douglas.jointlyapp.data.model.HomeListAdapter;
import com.douglas.jointlyapp.ui.JointlyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

/**
 * Entity Order adapter homeList by Date
 */
public class HomeListAdaptersSortByDate implements Comparator<HomeListAdapter>{
    @Override
    public int compare(HomeListAdapter o1, HomeListAdapter o2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_DD_MM_YYYY, Locale.getDefault());
        int result = 0;
        try {
            result = simpleDateFormat.parse(o1.getInitiative().getTarget_date()).compareTo(simpleDateFormat.parse(o2.getInitiative().getTarget_date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
