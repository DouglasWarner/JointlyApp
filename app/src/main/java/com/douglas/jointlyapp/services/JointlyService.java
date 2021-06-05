package com.douglas.jointlyapp.services;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JointlyService {

    @GET("api/test/")
    Call<APIResponse<Object>> testConnection();
}
