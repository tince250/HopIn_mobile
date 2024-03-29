package com.example.uberapp_tim13.rest;

import com.example.uberapp_tim13.dtos.CompleteRideReviewDTO;
import com.example.uberapp_tim13.dtos.ReviewDTO;
import com.example.uberapp_tim13.dtos.ReviewReturnedDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewAPI {
    @GET("review/{rideId}")
    Call<ArrayList<CompleteRideReviewDTO>> getAllRideReviews(@Header("Authorization") String token, @Path("rideId") int rideId);

    @POST("review/{rideId}/vehicle")
    Call<ReviewReturnedDTO> postVehicleReview(@Header("Authorization") String token,
                                              @Path("rideId") int rideId, @Body ReviewDTO review);

    @POST("review/{rideId}/driver")
    Call<ReviewReturnedDTO> postDriverReview(@Header("Authorization") String token,
                                            @Path("rideId") int rideId, @Body ReviewDTO review);
}
