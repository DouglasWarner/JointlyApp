package com.douglas.jointlyapp.data.comparators;

import com.douglas.jointlyapp.data.model.User;

import java.util.Comparator;

public class FavoriteSortByFollowers implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        return 0;
//        return Integer.compare(o1.getUserFollows(), o2.getUserFollows());
    }
}
