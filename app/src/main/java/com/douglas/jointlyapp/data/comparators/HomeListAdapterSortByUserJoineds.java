package com.douglas.jointlyapp.data.comparators;

import com.douglas.jointlyapp.data.model.HomeListAdapter;

import java.util.Comparator;

/**
 * Entity Order adapter homeList by UserJoineds
 */
public class HomeListAdapterSortByUserJoineds implements Comparator<HomeListAdapter> {

    @Override
    public int compare(HomeListAdapter o1, HomeListAdapter o2) {
        return Long.compare(o2.getCountUserJoined(), o1.getCountUserJoined());
    }
}
