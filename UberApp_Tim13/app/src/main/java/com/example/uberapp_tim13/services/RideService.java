package com.example.uberapp_tim13.services;
import android.util.Log;

import com.example.uberapp_tim13.dtos.AllPassengerRidesDTO;
import com.example.uberapp_tim13.dtos.RideDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.tools.Globals;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideService extends Service {

    public static boolean orderAgain = false;
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
                } else if (method.equals("getAllRides")){
                    int userId = (int) extras.get("userId");
                    String role = (String) extras.get("role");
                    getAllUserRides(userId, role);
                } else if (method.equals("activeRide")) {
                    getPassengerActiveRide();
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
        Log.d("ride", rideInCreation.toString());
        Call<RideReturnedDTO> call = RestUtils.rideAPI.orderRide(AuthService.tokenDTO.getAccessToken(), rideInCreation);
        call.enqueue((new Callback<RideReturnedDTO>() {
            @Override
            public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response) {
                returnedRide = response.body();
                if (response.code() == 200) {
                    Globals.currentRide = response.body();
                    sendOrderedRideBroadcast(response.body());
                }
                else if (response.code() == 400)
                    Log.d("greska", "greska");
            }

            @Override
            public void onFailure(Call<RideReturnedDTO> call, Throwable t) {
                t.printStackTrace();
                Log.d("REZ", t.getMessage());
            }
        }));
    }

    private void getAllUserRides(int userId, String role){
        Call<AllPassengerRidesDTO> call = null;
        Log.d("TOKEN", AuthService.tokenDTO.getAccessToken());
        if (role.equals("driver"))
            call = RestUtils.driverAPI.getAllRides(AuthService.tokenDTO.getAccessToken(), userId);
        else
            call = RestUtils.passengerAPI.getAllRides(AuthService.tokenDTO.getAccessToken(), userId);
        call.enqueue(new Callback<AllPassengerRidesDTO>() {

            @Override
            public void onResponse(Call<AllPassengerRidesDTO> call, Response<AllPassengerRidesDTO> response){
                Log.d("allRides", response.body().toString());
                sendAllUserRidesBroadcast(response.body());
            }

            @Override
            public void onFailure(Call<AllPassengerRidesDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void getPassengerActiveRide() {
        Call<RideReturnedDTO> call = RestUtils.rideAPI.getPassengerActiveRide(AuthService.tokenDTO.getAccessToken(), Globals.userId);
        call.enqueue(new Callback<RideReturnedDTO>() {
            @Override
            public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response) {
                Log.d("provjera", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    sendActiveRideBroadcast(true);
                } else {
                    sendActiveRideBroadcast(false);
                }
            }

            @Override
            public void onFailure(Call<RideReturnedDTO> call, Throwable t) {
                Log.d("EVOME", t.toString());
            }
        });
    }

    private void sendAllUserRidesBroadcast(AllPassengerRidesDTO rides){
        Intent intent = new Intent("rideHistoryFragment");
        intent.putExtra("allRides", rides);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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

    private void sendActiveRideBroadcast(boolean hasActive){
        Intent intent = new Intent ("activeRide");
        intent.putExtra("hasActive", hasActive);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
