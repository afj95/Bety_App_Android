package com.myprojects.bety2.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myprojects.bety2.classes.Home;
import com.myprojects.bety2.classes.Stuff;
import com.myprojects.bety2.classes.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiUrl {

    // Authentication routes
    @POST("au/register")
    Call<JsonObject> register(@Body User user);

    @POST("au/login")
    Call<JsonObject> logIn(@Body User user);

    @GET("au/getCurrentUser")
    Call<JsonObject> getCurrentUser(@Header("authorization") String authorization);

    @POST("au/logout")
    Call<Void> logOut(@Header("authorization") String authorization );

    // Homes routes
    @GET("homes")
    Call<JsonArray> getHomes(@Header("authorization") String authorization);

    @GET("homes/info/{homeId}")
    Call<JsonObject> getHomeInfo(@Path("homeId") String homeId, @Header("authorization") String authorization);

    @POST("homes/")
    Call<JsonObject> addHome(@Body Home home, @Header("authorization") String authorization);

    @DELETE("homes/del/{homeId}")
    Call<JsonObject> deleteHome(@Path("homeId") String homeId, @Header("authorization") String authorization);

    // Stuffs routes
    @GET("stuffs/{homeId}")
    Call<JsonArray> getStuffs(@Path("homeId") String homeId, @Header("authorization") String authorization);

    @POST("stuffs/add")
    Call<JsonObject> addStuff(@Body Stuff stuff);

    @PUT("au/update")
    Call<Void> updateUser(@Body User user, @Header("authorization") String authorization);

    // Old API

//    @POST("us/a-users/u-search/{homeId}/{memberEmail}")
//    Call<JsonObject> addNewMemberToHome(@Path("homeId") String homeId,
//                                        @Path("memberEmail") String userEmail);

//    @PUT("st/a-stuffs/s-delete-stuff/{stuffId}/{homeId}")
//    Call<JsonObject> deleteStuff(@Path("stuffId") String stuffId, @Path("homeId") String homeId);
}