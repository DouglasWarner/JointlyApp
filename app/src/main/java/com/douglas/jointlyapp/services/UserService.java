package com.douglas.jointlyapp.services;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserReviewUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserService {

    //region USER

    @GET("api/users/")
    Call<APIResponse<List<User>>> getListUser();

    @GET("api/users/user/")
    Call<APIResponse<User>> getUserByEmail(@Query("email") String email);

    @POST("api/users/user/")
    Call<APIResponse<User>> postUser(@Body String email, @Body String password, @Body String name,
                        @Body String phone, @Body byte[] imagen, @Body String location,
                        @Body String description);

    @PUT("api/users/user/")
    Call<APIResponse<User>> putUser(@Body String email, @Body String password, @Body String name,
                       @Body String phone, @Body byte[] imagen, @Body String location,
                       @Body String description, @Body int id);

    @GET("api/users/initiatives/created/")
    Call<APIResponse<List<Initiative>>> getListInitiativeCreated(@Query("email") String email);

    //endregion

    //region UserJoin

    @GET("api/users/initiatives/joined/")
    Call<APIResponse<List<Initiative>>> getListInitiativeJoinedByUser(@Query("email") String email, @Query("type") int type);

    //endregion

    //region UserFollow

    @GET("api/users/follows/")
    Call<APIResponse<List<UserFollowUser>>> getListUserFollow();

    @GET("api/users/followed/")
    Call<APIResponse<List<User>>> getUserFollowed(@Query("email") String email);

    @GET("api/users/followers/")
    Call<APIResponse<User>> getUserFollowers(@Query("email") String email);

    @POST("api/users/followed/")
    Call<APIResponse<UserFollowUser>> postUserFollow(@Query("userEmail") String userEmail, @Query("userFollowEmail") String userFollowEmail);

    @DELETE("api/users/followed/")
    Call<APIResponse<UserFollowUser>> delUserFollow(@Query("userEmail") String userEmail, @Query("userFollowEmail") String userFollowEmail);

    //endregion

    //region UserReview

    @GET("api/users/reviews/")
    Call<APIResponse<List<UserReviewUser>>> getListUserReview();

    @GET("api/users/reviews/")
    Call<APIResponse<List<UserReviewUser>>> getListUserReview(@Query("email") String email);

    @POST("api/users/reviews/")
    Call<APIResponse<UserReviewUser>> postUserReview(@Body UserReviewUser userReviewUser);

//    String date, @Body String userEmail,
//    @Body String userReviewEmail,
//    @Body String review, @Body int stars

    @POST("api/users/follows/sync/")
    Call<APIResponse<UserFollowUser>> syncToAPI(@Body List<UserFollowUser> followUserList);

    //endregion
}
