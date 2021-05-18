package com.douglas.jointlyapp.services;


import retrofit2.Call;
import retrofit2.http.GET;

public interface InitiativeService {

    @GET("api/initiatives/")
    Call<APIResponse> getInitiative();
}
