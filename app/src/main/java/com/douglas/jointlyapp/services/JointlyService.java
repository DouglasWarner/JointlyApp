package com.douglas.jointlyapp.services;

import com.douglas.jointlyapp.data.model.Chat;
import com.douglas.jointlyapp.data.model.Countries;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.TargetArea;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.model.UserReviewUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Interface that represent api request
 */
public interface JointlyService {

    @GET("api/test/")
    Call<APIResponse<Object>> testConnection();

    @GET("api/targetAreas/")
    Call<APIResponse<List<TargetArea>>> getListTargetArea();

    @GET("api/countries/")
    Call<APIResponse<List<Countries>>> getListCountries();

    //region SYNC

    @POST("api/initiatives/initiative/sync/")
    Call<APIResponse<Initiative>> syncInitiativeToAPI(@Body List<Initiative> initiativeList);

    @POST("api/initiatives/userjoin/sync/")
    Call<APIResponse<UserJoinInitiative>> syncUserJoinInitiativeToAPI(@Body List<UserJoinInitiative> joinInitiativeList);

    @POST("api/users/follows/sync/")
    Call<APIResponse<UserFollowUser>> syncUserFollowToAPI(@Body List<UserFollowUser> followUserList);

    @POST("api/users/userreview/sync/")
    Call<APIResponse<UserReviewUser>> syncUserReviewToAPI(@Body List<UserReviewUser> reviewUserList);

    @POST("api/chat/sync/")
    Call<APIResponse<Chat>> syncChatToAPI(@Body List<Chat> chatList);

    //endregion
}
