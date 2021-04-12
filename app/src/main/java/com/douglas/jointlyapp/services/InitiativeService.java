package com.douglas.jointlyapp.services;


import com.douglas.jointlyapp.data.model.Initiative;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InitiativeService {

    @GET("list/")
    Call<List<Initiative>> getInitiative();
}
