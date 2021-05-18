package com.douglas.jointlyapp.services;

import com.douglas.jointlyapp.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {

    @GET("")
    Call<List<User>> getUser();
}
