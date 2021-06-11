package com.douglas.jointlyapp.data.comparators;

import com.douglas.jointlyapp.data.model.HomeListAdapter;

import java.util.Comparator;

public class HomeListAdapterSortByLocation implements Comparator<HomeListAdapter> {

    @Override
    public int compare(HomeListAdapter o1, HomeListAdapter o2) {
        return o1.getInitiative().getLocation().compareTo(o2.getInitiative().getLocation());
    }
}
