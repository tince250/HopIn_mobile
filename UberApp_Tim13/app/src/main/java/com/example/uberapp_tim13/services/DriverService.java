package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.dtos.AllPassengerRidesDTO;
import com.example.uberapp_tim13.dtos.VehicleDTO;
import com.example.uberapp_tim13.rest.RestUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        String method = (String) extras.get("method");
        int driverId = (int) extras.get("driverId");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (method.equals("getAllRides")){
                    getAllRides(driverId);
                } else if (method.equals("getVehicle")){
                    getVehicle(driverId);
                }
            }
        });
        stopSelf();
        return START_NOT_STICKY;
    }

    private void getAllRides(int driverId){
        Call<AllPassengerRidesDTO> call = RestUtils.driverAPI.getAllRides("",
                driverId, 0, 2, "", "", "");
        call.enqueue(new Callback<AllPassengerRidesDTO>() {

            @Override
            public void onResponse(Call<AllPassengerRidesDTO> call, Response<AllPassengerRidesDTO> response){
                Log.d("allRides", response.body().toString());
                sendAllRidesBroadcast(response.body());
            }

            @Override
            public void onFailure(Call<AllPassengerRidesDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void getVehicle(int driverId){
        Call<VehicleDTO> call = RestUtils.driverAPI.getVehicle("",
                driverId);
        call.enqueue(new Callback<VehicleDTO>() {

            @Override
            public void onResponse(Call<VehicleDTO> call, Response<VehicleDTO> response){
                Log.d("allRides", response.body().toString());
                sendVehicleBroadcast(response.body());
            }

            @Override
            public void onFailure(Call<VehicleDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void sendAllRidesBroadcast(AllPassengerRidesDTO rides){
        Intent intent = new Intent("rideHistoryFragment");
        intent.putExtra("allRides", rides);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendVehicleBroadcast(VehicleDTO vehicleDTO){
        Intent intent = new Intent("userDetailsDialog");
        intent.putExtra("vehicle", vehicleDTO);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
