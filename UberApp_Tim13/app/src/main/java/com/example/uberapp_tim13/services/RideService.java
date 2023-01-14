package com.example.uberapp_tim13.services;

<<<<<<< HEAD
import android.util.Log;
import android.widget.Toast;

import com.example.uberapp_tim13.dtos.RideDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.rest.RestUtils;

=======
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.dtos.CredentialsDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.TokenDTO;
import com.example.uberapp_tim13.rest.RestUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

>>>>>>> feature/driver-ride-history
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

<<<<<<< HEAD
public class RideService {
    public static RideDTO rideInCreation = new RideDTO();
    public static RideReturnedDTO returnedRide;
    public static boolean success = false;

    public RideService() {
    }

    public void orderRide() {
        Call<RideReturnedDTO> call = RestUtils.rideApi.orderRide(rideInCreation);
        call.enqueue(new Callback<RideReturnedDTO>() {
            @Override
            public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response) {
                if (response.code() == 200) {
                    Log.d("REZ", response.body().toString());
                    returnedRide = response.body();
                    success = true;
                }
=======
public class RideService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                getRideById();
            }
        });

        return START_NOT_STICKY;
    }

    public void getRideById(){
        Call<RideReturnedDTO> call = RestUtils.rideAPI.getRideById("");
        call.enqueue(new Callback<RideReturnedDTO>() {

            @Override
            public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response){
                Log.d("PAPAPA", response.body().toString());
                sendRideByIdBroadcast(response.body());
>>>>>>> feature/driver-ride-history
            }

            @Override
            public void onFailure(Call<RideReturnedDTO> call, Throwable t) {
<<<<<<< HEAD
                success = false;

=======
                Log.d("PAPAPAEEE", "ERROR");
>>>>>>> feature/driver-ride-history
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }
<<<<<<< HEAD
=======

    private void sendRideByIdBroadcast(RideReturnedDTO ride){
        Log.d("PAPAPA", ride.toString());
        Intent intent = new Intent ("getRideById");
        intent.putExtra("rideById", ride);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
>>>>>>> feature/driver-ride-history
}
