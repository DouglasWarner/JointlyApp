package com.douglas.jointlyapp.data.comparators;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;

import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;

public class InitiativeSortByLocation implements Comparator<Initiative> {

    @Override
    public int compare(Initiative o1, Initiative o2) {
        return o1.getLocation().compareTo(o2.getLocation());
    }

}
