package com.example.uberapp_tim13.rest;

<<<<<<< HEAD
import com.example.uberapp_tim13.dtos.RideDTO;
=======
>>>>>>> feature/driver-ride-history
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserDTO;

import retrofit2.Call;
<<<<<<< HEAD
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RideAPI {

    @POST("ride")
    Call<RideReturnedDTO> orderRide(@Body RideDTO ride);

=======
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RideAPI {

    @GET(RestUtils.RIDE_GET_ID)
    Call<RideReturnedDTO> getRideById(@Header("Authorization") String token);
>>>>>>> feature/driver-ride-history
}
