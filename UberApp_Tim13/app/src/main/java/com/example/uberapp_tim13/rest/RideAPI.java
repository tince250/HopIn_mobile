package com.example.uberapp_tim13.rest;

import com.example.uberapp_tim13.dtos.RideDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RideAPI {

    @POST("ride")
    Call<RideReturnedDTO> orderRide(@Body RideDTO ride);

}
