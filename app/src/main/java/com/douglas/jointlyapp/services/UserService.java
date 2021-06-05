package com.douglas.jointlyapp.services;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserReviewUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    //region USER

    @GET("api/users/")
    Call<APIResponse<User>> getListUser();

    @GET("api/users/user/")
    Call<APIResponse<User>> getUserByEmail(@Body String email);

    @POST("api/users/user/")
    Call<APIResponse<User>> postUser(@Body String email, @Body String password, @Body String name,
                        @Body String phone, @Body byte[] imagen, @Body String location,
                        @Body String description);

    @PUT("api/users/user/")
    Call<APIResponse<User>> putUser(@Body String email, @Body String password, @Body String name,
                       @Body String phone, @Body byte[] imagen, @Body String location,
                       @Body String description, @Body int id);

    @GET("api/users/initiatives/created/")
    Call<APIResponse<Initiative>> getListInitiativeCreated(@Query("email") String email);

    //endregion

    //region UserJoin

    @GET("api/users/initiatives/joined/")
    Call<APIResponse<Initiative>> getListInitiativeJoinedByUser(@Query("email") String email, @Query("type") int type);

    //endregion

    //region UserFollow

    @GET("api/users/follows/")
    Call<APIResponse<UserFollowUser>> getListUserFollow();

    @GET("api/users/followed/")
    Call<APIResponse<UserFollowUser>> getUserFollowed(@Body String email);

    @GET("api/users/followers/")
    Call<APIResponse<UserFollowUser>> getUserFollowers(@Body String email);

    @POST("api/users/followed/")
    Call<APIResponse<UserFollowUser>> postUserFollow(@Body String userEmail, @Body String userFollowEmail);

    @DELETE("api/users/followed/")
    Call<APIResponse<UserFollowUser>> delUserFollow(@Body String userEmail, @Body String userFollowEmail);

    //endregion

    //region UserReview

    @GET("api/users/reviews/")
    Call<APIResponse<UserReviewUser>> getListUserReview();

    @GET("api/users/reviews/")
    Call<APIResponse<UserReviewUser>> getUserReview(@Body String email);

    @POST("api/users/reviews/")
    Call<APIResponse<UserReviewUser>> postUserReview(@Body String date, @Body String userEmail,
                                        @Body String userReviewEmail,
                                        @Body String review, @Body int stars);

    //endregion
}
