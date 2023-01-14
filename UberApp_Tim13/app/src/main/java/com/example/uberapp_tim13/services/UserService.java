package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.rest.RestUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService extends Service{

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        String method = (String) extras.get("method");
        int userId = (int) extras.get("userId");
        String sender = (String) extras.get("sender");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (method.equals("getById")){
                    getById(userId, method);
                } else if (method.equals("getUserName"))
                    getById(userId, method);
            }
        });
        stopSelf();
        return START_NOT_STICKY;
    }

    private void getById(int userId, String method){
        Call<UserDTO> call = RestUtils.userApi.doGetUser("", userId);
        Log.d("userid", String.valueOf(userId));
        call.enqueue(new Callback<UserDTO>() {

            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response){
                Log.d("allDrivers", response.body().toString());
                if (method.equals("getById"))
                    sendUserByIdBroadcast(response.body());
                else if (method.equals("getUserName"))
                    sendUserNameBroadcast(response.body());
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }


    private void sendUserByIdBroadcast(UserDTO user){
        Intent intent = new Intent("userDetailsDialog");
        intent.putExtra("userById", user);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendUserNameBroadcast(UserDTO user){
        Intent intent = new Intent("ratingsAdapter");
        intent.putExtra("userName", user.getName() + " " + user.getSurname());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}