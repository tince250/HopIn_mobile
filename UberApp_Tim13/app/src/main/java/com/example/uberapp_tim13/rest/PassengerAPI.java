package com.example.uberapp_tim13.rest;

import com.example.uberapp_tim13.dtos.AllPassengerRidesDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.dtos.RideForReportDTO;
import com.example.uberapp_tim13.dtos.RouteDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("passenger/{id}/ride/date")
    Call<List<RideForReportDTO>> getAllRidesBetweenDates(@Header("Authorization") String token,
                                                         @Path("id") int id,
                                                         @Query("from") String from,
                                                         @Query("to") String to);

    @GET("passenger/{id}/favouriteRoutes")
    Call<List<RouteDTO>> getFavouriteRoutes(@Header("Authorization") String token,
                                            @Path("id") int id);

    @POST("passenger/{passengerId}/remove/route")
    Call<Void> removeFavouriteRoute(@Header("Authorization") String token,
                                    @Path("passengerId") int id,
                                    @Query("routeId") int routeId);

    @PUT("passenger/{id}")
    Call<UserDTO> update(@Header("Authorization") String token,
                         @Path("id") int id,
                         @Body UserDTO dto);
}
