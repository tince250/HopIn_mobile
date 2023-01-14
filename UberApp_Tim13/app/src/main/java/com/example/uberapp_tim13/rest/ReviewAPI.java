package com.example.uberapp_tim13.rest;

import com.example.uberapp_tim13.dtos.reviews.CompleteRideReviewDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReviewAPI {
    @GET("review/{rideId}")
    Call<ArrayList<CompleteRideReviewDTO>> getAllRideReviews(@Path("rideId") int rideId);

}
