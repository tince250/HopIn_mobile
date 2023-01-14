package com.example.uberapp_tim13.rest;

import com.example.uberapp_tim13.dtos.RideDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RideAPI {

    @GET(RestUtils.RIDE_GET_ID)
    Call<RideReturnedDTO> getRideById(@Header("Authorization") String token);

     @POST("ride")
    Call<RideReturnedDTO> orderRide(@Body RideDTO ride);
}
