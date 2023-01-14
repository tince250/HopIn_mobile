package com.example.uberapp_tim13.rest;
import com.example.uberapp_tim13.dtos.PanicRideDTO;
import com.example.uberapp_tim13.dtos.ReasonDTO;
import com.example.uberapp_tim13.dtos.RideDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.model.Ride;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RideAPI {

    @POST("ride")
    Call<RideReturnedDTO> orderRide(@Body RideDTO ride);

    @GET(RestUtils.RIDE_GET_ID)
    Call<RideReturnedDTO> getRideById(@Header("Authorization") String token);

    @GET("ride/{id}")
    Call<RideReturnedDTO> getRideByIdOnly(@Path("id") int id);

    @PUT("ride/{id}/panic")
    Call<PanicRideDTO> panicRide(@Path("id") int rideId, @Body ReasonDTO reason);

    @PUT("ride/{id}/start")
    Call<RideReturnedDTO> startRide(@Path("id") int rideId);

    @PUT("ride/{id}/end")
    Call<RideReturnedDTO> finishRide(@Path("id") int rideId);

}
