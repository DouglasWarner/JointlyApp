package com.douglas.jointlyapp.data.comparators;

import com.douglas.jointlyapp.data.model.User;

import java.util.Comparator;

public class FavoriteSortByName implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
