package com.example.uberapp_tim13.rest;

import com.example.uberapp_tim13.dtos.AllPassengerRidesDTO;
import com.example.uberapp_tim13.dtos.VehicleDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DriverAPI {

    @GET("driver/{id}/ride")
    Call<AllPassengerRidesDTO> getAllRidesPaginated(@Header("Authorization") String token,
                                           @Path("id") int id,
                                           @Query("page") int page,
                                           @Query("size") int size,
                                           @Query("sort") String sort,
                                           @Query("from") String from,
                                           @Query("to") String to);

    @GET("driver/{id}/all/rides")
    Call<AllPassengerRidesDTO> getAllRides(@Header("Authorization") String token,
                                           @Path("id") int id);

    @GET("driver/{id}/vehicle")
    Call<VehicleDTO> getVehicle(@Header("Authorization") String token,
                                @Path("id") int id);
}
