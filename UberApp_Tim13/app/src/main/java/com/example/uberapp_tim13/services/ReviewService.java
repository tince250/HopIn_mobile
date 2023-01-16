package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.dtos.CompleteRideReviewDTO;
import com.example.uberapp_tim13.dtos.ReviewDTO;
import com.example.uberapp_tim13.dtos.ReviewReturnedDTO;
import com.example.uberapp_tim13.rest.RestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        String method = (String) extras.get("method");
        int rideId = (int) extras.get("rideId");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (method.equals("calculateRideReviews"))
                    getAllRideReviews(rideId, method);
                else if (method.equals("getAllReviews"))
                    getAllRideReviews(rideId, method);
                else if (method.equals("postBothReviews")){
                    ReviewDTO review = (ReviewDTO) extras.get("driverReview");
                    postVehicleReview(rideId, review);
                    review = (ReviewDTO) extras.get("vehicleReview");
                    postDriverReview(rideId, review);
                }
            }
        });
        stopSelf();
        return START_NOT_STICKY;
    }

    private void getAllRideReviews(int rideId, String method) {
        Call<ArrayList<CompleteRideReviewDTO>> call = RestUtils.reviewAPI.getAllRideReviews(rideId);
        final ArrayList<CompleteRideReviewDTO>[] reviews = new ArrayList[]{null};
        call.enqueue(new Callback<ArrayList<CompleteRideReviewDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<CompleteRideReviewDTO>> call, Response<ArrayList<CompleteRideReviewDTO>> response){
                reviews[0] = response.body();
                Log.d("reviews", response.body().toString());
                if (method.equals("calculateRideReviews"))
                    sendAllReviewsCalculationBroadcast(reviews[0]);
                if (method.equals("getAllReviews"))
                    sendAllReviewsBroadcast(reviews[0]);
            }

            @Override
            public void onFailure(Call<ArrayList<CompleteRideReviewDTO>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void postVehicleReview(int rideId, ReviewDTO review){
        Call<ReviewReturnedDTO> call = RestUtils.reviewAPI.postVehicleReview(
                "Bearer: eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJIb3BJbiIsInN1YiI6InBlcmFAZ21haWwuY29tIiwicm9sZSI6W3siYXV0aG9yaXR5IjoiUk9MRV9QQVNTRU5HRVIifV0sImlkIjoxLCJhdWQiOiJ3ZWIiLCJpYXQiOjE2NzM4MDA3NTksImV4cCI6MTY3MzgwMjU1OX0.NOdzgtlZH5Sxrmdmb_KwAkyjDAx7ZxmIPf-494edLz7oGi3FmcH28lfBoe4lGddQDuPVMcVF2nkQrXLC1zk0iQ", 3, review);
        call.enqueue(new Callback<ReviewReturnedDTO>() {

            @Override
            public void onResponse(Call<ReviewReturnedDTO> call, Response<ReviewReturnedDTO> response){
                if (response.code() == 200){
                    sendPostedReviewBroadcast("driver");
                }
            }

            @Override
            public void onFailure(Call<ReviewReturnedDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void postDriverReview(int rideId, ReviewDTO review){
        Call<ReviewReturnedDTO> call = RestUtils.reviewAPI.postDriverReview(
                "Bearer: eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJIb3BJbiIsInN1YiI6InBlcmFAZ21haWwuY29tIiwicm9sZSI6W3siYXV0aG9yaXR5IjoiUk9MRV9QQVNTRU5HRVIifV0sImlkIjoxLCJhdWQiOiJ3ZWIiLCJpYXQiOjE2NzM4MDA3NTksImV4cCI6MTY3MzgwMjU1OX0.NOdzgtlZH5Sxrmdmb_KwAkyjDAx7ZxmIPf-494edLz7oGi3FmcH28lfBoe4lGddQDuPVMcVF2nkQrXLC1zk0iQ", 3, review);
        call.enqueue(new Callback<ReviewReturnedDTO>() {

            @Override
            public void onResponse(Call<ReviewReturnedDTO> call, Response<ReviewReturnedDTO> response){
                if (response.code() == 200){
                    sendPostedReviewBroadcast("vehicle");
                }
            }

            @Override
            public void onFailure(Call<ReviewReturnedDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void sendPostedReviewBroadcast(String method){
        Intent intent = new Intent("rateRideDialog");
        if (method.equals("vehicle"))
            intent.putExtra("postedVehicle", true);
        else
            intent.putExtra("postedDriver", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendAllReviewsBroadcast(ArrayList<CompleteRideReviewDTO> reviews){
        Intent intent = new Intent("rideHistoryDetailsFragment");
        intent.putExtra("allReviews", reviews);
        intent.putExtra("allRideReviewsRating", calculateRating(reviews));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendAllReviewsCalculationBroadcast(ArrayList<CompleteRideReviewDTO> reviews){
        Intent intent = new Intent("historyAdapter");
        intent.putExtra("allRideReviewsRating", calculateRating(reviews));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private float calculateRating(List<CompleteRideReviewDTO> reviews){
        float rating = 0;
        int counter = 0;
        for (CompleteRideReviewDTO review : reviews){
            if (review.getDriverReview() != null){
                counter++;
                rating += review.getDriverReview().getRating();
            }
            if (review.getVehicleReview() != null) {
                counter++;
                rating += review.getVehicleReview().getRating();
            }
        }
        return rating/counter;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
