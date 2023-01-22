package com.example.uberapp_tim13.rest;

import com.example.uberapp_tim13.dtos.AllPassengerRidesDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PassengerAPI {

    @GET("passenger/{id}/ride")
    Call<AllPassengerRidesDTO> getAllRides(@Header("Authorization") String token,
                                           @Path("id") int id,
                                           @Query("page") int page,
                                           @Query("size") int size,
                                           @Query("sort") String sort,
                                           @Query("from") String from,
                                           @Query("to") String to);

    @POST("passenger")
    Call<UserReturnedDTO> register(@Body UserDTO user);

}
