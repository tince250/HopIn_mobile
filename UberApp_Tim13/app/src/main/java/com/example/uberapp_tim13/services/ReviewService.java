package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.dtos.AllPassengerRidesDTO;
import com.example.uberapp_tim13.dtos.CompleteRideReviewDTO;
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
            }
        });

        return START_NOT_STICKY;
    }

    private void getAllRideReviews(int rideId, String method) {
        Call<ArrayList<CompleteRideReviewDTO>> call = RestUtils.reviewAPI.getAllRideReviews(rideId);
        final ArrayList<CompleteRideReviewDTO>[] reviews = new ArrayList[]{null};
        Log.d("usao", "jeee");
        call.enqueue(new Callback<ArrayList<CompleteRideReviewDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<CompleteRideReviewDTO>> call, Response<ArrayList<CompleteRideReviewDTO>> response){
                reviews[0] = response.body();
                Log.d("reviews", response.body().toString());
                if (method.equals("calculateRideReviews"))
                    sendAllReviewsBroadcast(reviews[0]);
            }

            @Override
            public void onFailure(Call<ArrayList<CompleteRideReviewDTO>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void sendAllReviewsBroadcast(ArrayList<CompleteRideReviewDTO> reviews){
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
        Log.d("rating", String.valueOf(rating));
        Log.d("counter", String.valueOf(counter));
        return rating/counter;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
