package com.example.uberapp_tim13.services;
import android.util.Log;
import android.widget.Toast;

import com.example.uberapp_tim13.dtos.RideDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.rest.RestUtils;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.dtos.CredentialsDTO;
import com.example.uberapp_tim13.dtos.RideDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.TokenDTO;
import com.example.uberapp_tim13.rest.RestUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;

public class RideService extends Service {

    public static RideDTO rideInCreation = new RideDTO();
    public static RideReturnedDTO returnedRide;
    public static boolean finished = false;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        String method = (String) extras.get("method");

        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (method.equals("getRideById")) {
                    getRideById();
                } else if (method.equals("orderRide")) {
                    orderRide();
                }
            }
        });

        return START_NOT_STICKY;
    }

    public void getRideById(){
        Call<RideReturnedDTO> call = RestUtils.rideAPI.getRideById("");
        call.enqueue(new Callback<RideReturnedDTO>() {

            @Override
            public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response){
                finished = false;
                Log.d("PAPAPA", response.body().toString());
                sendRideByIdBroadcast(response.body());
                 finished = true;
            }

            @Override
            public void onFailure(Call<RideReturnedDTO> call, Throwable t) {

                Log.d("PAPAPAEEE", "ERROR");
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }


    public void orderRide() {
        finished = false;
        Call<RideReturnedDTO> call = RestUtils.rideAPI.orderRide(rideInCreation);
        call.enqueue((new Callback<RideReturnedDTO>() {
            @Override
            public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response) {
                returnedRide = response.body();
                Log.d("REZ", returnedRide.toString());
                sendOrderedRideBroadcast(response.body());
            }

            @Override
            public void onFailure(Call<RideReturnedDTO> call, Throwable t) {
                t.printStackTrace();
                Log.d("REZ", t.getMessage());
            }
        }));
    }

    private void sendRideByIdBroadcast(RideReturnedDTO ride){
        Log.d("PAPAPA", ride.toString());
        Intent intent = new Intent ("getRideById");
        intent.putExtra("rideById", ride);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendOrderedRideBroadcast(RideReturnedDTO ride){
        Log.d("PAPAPA", ride.toString());
        Intent intent = new Intent ("orderedRide");
        intent.putExtra("orderedRide", ride);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
