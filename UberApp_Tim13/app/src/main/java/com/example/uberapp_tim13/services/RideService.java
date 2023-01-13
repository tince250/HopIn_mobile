package com.example.uberapp_tim13.services;

import android.util.Log;
import android.widget.Toast;

import com.example.uberapp_tim13.dtos.RideDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.rest.RestUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            }

            @Override
            public void onFailure(Call<RideReturnedDTO> call, Throwable t) {
                success = false;

                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }
}
