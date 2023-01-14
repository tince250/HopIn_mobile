package com.example.uberapp_tim13.rest;

import com.example.uberapp_tim13.dtos.CompleteRideReviewDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ReviewAPI {
    @GET("review/{rideId}")
    Call<ArrayList<CompleteRideReviewDTO>> getAllRideReviews(@Path("rideId") int rideId);

}
