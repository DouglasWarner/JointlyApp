package com.douglas.jointlyapp.data.comparators;

import com.douglas.jointlyapp.data.model.Initiative;

import java.util.Comparator;

public class InitiativeSortByUserJoineds implements Comparator<Initiative> {
    @Override
    public int compare(Initiative o1, Initiative o2) {
        return Integer.compare(o1.getCountUserJoined(), o2.getCountUserJoined());
    }
}
