package com.douglas.jointlyapp.ui.base;

public interface BaseProfileView<T> {
    void onSuccess(T user, int countUserFollowers, int initiativeCreated, int initiativeJoined);
}
