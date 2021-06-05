package com.douglas.jointlyapp.services;


import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface InitiativeService {

    //region INITIATIVE

    @GET("api/initiatives/")
    Call<APIResponse<Initiative>> getListInitiative();

    @GET("api/initiatives/listByName/")
    Call<APIResponse<Initiative>> getListInitiativeByName(@Body String name);

    @GET("api/initiatives/initiative/")
    Call<APIResponse<Initiative>> getInitiative(@Body int id);

    @POST("api/initiatives/initiative/")
    Call<APIResponse<Initiative>> postInitiative(@Query("name") String name, @Query("created_at") String createdAt, @Query("target_date") String targetDate,
                                                 @Query("description") String description, @Query("target_area") String targetArea, @Query("location") String location,
                                                 @Query("imagen") byte[] imagen, @Query("target_amount") int targetAmount,
                                                 @Query("created_by") String createdBy);

    @PUT("api/initiatives/initiative/")
    Call<APIResponse<Initiative>> putInitiative(@Query("name") String name, @Query("targetDate") String targetDate, @Query("description") String description,
                                                @Query("targetArea") String targetArea, @Query("location") String location, @Query("imagen") byte[] imagen,
                                                @Query("targetAmount") int targetAmount, @Query("id") long id);

    @DELETE("api/initiatives/initiative/")
    Call<APIResponse<Initiative>> delInitiative(@Query("id") long id);

    //endregion

    //region User

    @GET("api/initiatives/usersJoinedByInitiative/")
    Call<APIResponse<User>> getListUsersJoinedByInitiative(@Query("idInitiative") long idInitiative);

    //endregion

    //region UserJoin

    @GET("api/initiatives/usersJoined/")
    Call<APIResponse<UserJoinInitiative>> getListUserJoined();

    @POST("api/initiatives/usersJoined/")
    Call<APIResponse<UserJoinInitiative>> postUsersJoined(@Body String date, @Body int idInitiative,
                                             @Body String userEmail, @Body int type);

    @PUT("api/initiatives/usersJoined/")
    Call<APIResponse<UserJoinInitiative>> putUsersJoined(@Body String date, @Body int idInitiative,
                                            @Body String userEmail, @Body int type);

    @DELETE("api/initiatives/usersJoined/")
    Call<APIResponse<UserJoinInitiative>> delUsersJoined(@Body String date, @Body int idInitiative,
                                            @Body String userEmail);

    //endregion
}
