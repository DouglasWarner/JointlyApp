package com.douglas.jointlyapp.services;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Interface that represent api request
 */
public interface InitiativeService {

    //region INITIATIVE

    @GET("api/initiatives/")
    Call<APIResponse<List<Initiative>>> getListInitiative();

    @GET("api/initiatives/initiative/")
    Call<APIResponse<Initiative>> getInitiative(@Query("id") int id);

    @Multipart
    @POST("api/initiatives/initiative/")
    Call<APIResponse<Initiative>> postInitiativeWithImage(@Query("name") String name, @Query("created_at") String createdAt, @Query("target_date") String targetDate,
                                                          @Query("description") String description, @Query("target_area") String targetArea, @Query("location") String location,
                                                          @Query("target_amount") int targetAmount, @Query("created_by") String createdBy, @Query("ref_code") String ref_code,
                                                          @Part MultipartBody.Part file);

    @Multipart
    @POST("api/initiatives/initiative/")
    Call<APIResponse<Initiative>> postInitiativeWithoutImage(@Query("name") String name, @Query("created_at") String createdAt, @Query("target_date") String targetDate,
                                                          @Query("description") String description, @Query("target_area") String targetArea, @Query("location") String location,
                                                          @Query("target_amount") int targetAmount, @Query("created_by") String createdBy, @Query("ref_code") String ref_code);

    @Multipart
    @PUT("api/initiatives/initiative/")
    Call<APIResponse<Initiative>> putInitiativeWithImage(@Query("name") String name, @Query("targetDate") String targetDate, @Query("description") String description,
                                                         @Query("targetArea") String targetArea, @Query("location") String location, @Query("targetAmount") int targetAmount,
                                                         @Part MultipartBody.Part file, @Query("id") long id);

    @PUT("api/initiatives/initiative/")
    Call<APIResponse<Initiative>> putInitiativeWithoutImage(@Query("name") String name, @Query("targetDate") String targetDate, @Query("description") String description,
                                                            @Query("targetArea") String targetArea, @Query("location") String location, @Query("targetAmount") int targetAmount,
                                                            @Query("id") long id);

    @DELETE("api/initiatives/initiative/")
    Call<APIResponse<Initiative>> delInitiative(@Query("id") long id);

    //endregion

    //region User

    @GET("api/initiatives/usersJoinedByInitiative/")
    Call<APIResponse<List<User>>> getListUsersJoinedByInitiative(@Query("idInitiative") long idInitiative);

    //endregion

    //region UserJoin

    @GET("api/initiatives/usersJoined/")
    Call<APIResponse<List<UserJoinInitiative>>> getListUserJoined();

    @POST("api/initiatives/usersJoined/")
    Call<APIResponse<UserJoinInitiative>> postUsersJoined(@Query("date") String date, @Query("idInitiative") long idInitiative,
                                             @Query("userEmail") String userEmail, @Query("type") int type);

    @PUT("api/initiatives/usersJoined/")
    Call<APIResponse<UserJoinInitiative>> putUsersJoined(@Query("idInitiative") long idInitiative, @Query("userEmail") String userEmail,
                                                         @Query("type") int type);

    @DELETE("api/initiatives/usersJoined/")
    Call<APIResponse<UserJoinInitiative>> delUsersJoined(@Query("idInitiative") long idInitiative, @Query("userEmail") String userEmail);

    //endregion
}
