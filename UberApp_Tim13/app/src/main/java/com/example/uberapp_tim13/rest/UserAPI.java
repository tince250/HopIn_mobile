package com.example.uberapp_tim13.rest;

import com.example.uberapp_tim13.dtos.UserDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserAPI {

    @GET(RestUtils.USER_GET)
    Call<UserDTO> doGetUser();

    @GET("user/email?")
    Call<UserDTO> getUserByEmail(@Query("email") String email);
}
