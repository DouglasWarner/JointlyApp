package com.douglas.jointlyapp.services;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserReviewUser;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
public interface UserService {

    //region USER

    @GET("api/users/")
    Call<APIResponse<List<User>>> getListUser();

    @GET("api/users/user/")
    Call<APIResponse<User>> getUserByEmail(@Query("email") String email);

    @POST("api/users/user/")
    Call<APIResponse<User>> postUser(@Query("email") String email, @Query("password") String password, @Query("name") String name,
                                     @Query("phone") String phone, @Query("location") String location,
                                     @Query("description") String description, @Query("created_at") String created_at);

    @Multipart
    @POST("api/users/user/")
    Call<APIResponse<User>> postUserWithImage(@Query("email") String email, @Query("password") String password, @Query("name") String name,
                                              @Query("phone") String phone, @Query("location") String location,
                                              @Query("description") String description, @Query("created_at") String created_at,
                                              @Part("file") MultipartBody.Part file);

    @Multipart
    @PUT("api/users/user/")
    Call<APIResponse<User>> putUserWithoutImage(@Query("email") String email, @Query("password") String password, @Query("name") String name,
                                    @Query("phone") String phone, @Query("location") String location,
                                    @Query("description") String description, @Query("id") long id);

    @Multipart
    @PUT("api/users/user/")
    Call<APIResponse<User>> putUserWithImage(@Query("email") String email, @Query("password") String password, @Query("name") String name,
                                    @Query("phone") String phone, @Query("location") String location,
                                    @Query("description") String description, @Part("file") MultipartBody.Part file, @Query("id") long id);

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

    //endregion
}
