package com.douglas.jointlyapp.ui.base;

public interface BaseProfileView<T> {
    void onSuccess(T user, long countUserFollowers, int initiativeCreated, int initiativeJoined);
}
