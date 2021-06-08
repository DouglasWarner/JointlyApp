package com.douglas.jointlyapp.services;

import com.douglas.jointlyapp.data.model.Countries;
import com.douglas.jointlyapp.data.model.TargetArea;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JointlyService {

    @GET("api/test/")
    Call<APIResponse<Object>> testConnection();

    @GET("api/targetAreas/")
    Call<APIResponse<List<TargetArea>>> getListTargetArea();

    @GET("api/countries/")
    Call<APIResponse<List<Countries>>> getListCountries();
}
