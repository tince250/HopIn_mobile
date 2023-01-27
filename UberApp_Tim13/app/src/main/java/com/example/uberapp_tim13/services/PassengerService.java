package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.dtos.VehicleDTO;
import com.example.uberapp_tim13.rest.RestUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        String method = (String) extras.get("method");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (method.equals("register")){
                    UserDTO user = (UserDTO) extras.get("user");
                    register(user);
                }
            }
        });
        stopSelf();
        return START_NOT_STICKY;
    }

    private void register(UserDTO newUser) {
        Call<UserReturnedDTO> call = RestUtils.passengerAPI.register(newUser);
        call.enqueue(new Callback<UserReturnedDTO>() {

            @Override
            public void onResponse(Call<UserReturnedDTO> call, Response<UserReturnedDTO> response){
                Log.d("rez", String.valueOf(response.code()));
                if (response.code() == 200)
                    sendResponseBroadcast(true);
                else
                    sendResponseBroadcast(false);
            }

            @Override
            public void onFailure(Call<UserReturnedDTO> call, Throwable t) {
                Log.d("rez", t.getMessage() != null ? t.getMessage() : "error");
                sendResponseBroadcast(false);
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendResponseBroadcast(boolean success){
        Intent intent = new Intent("registerActivity");
        intent.putExtra("success", success);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
