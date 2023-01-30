package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.example.uberapp_tim13.activities.DriverMainActivity;
import com.example.uberapp_tim13.dtos.CredentialsDTO;
import com.example.uberapp_tim13.dtos.TokenDTO;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.dtos.WorkingHoursDTO;
import com.example.uberapp_tim13.dtos.WorkingHoursStartDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.JWTUtils;

import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService extends Service {

    public static TokenDTO tokenDTO;
//    int id;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        CredentialsDTO credentialsDTO = (CredentialsDTO) extras.get("credentials");
//        id = extras.getInt("id");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                login(credentialsDTO);
            }
        });

        stopSelf();

        return START_NOT_STICKY;
    }

    public void login(CredentialsDTO credentialsDTO){
        Call<TokenDTO> call = RestUtils.userApi.login(credentialsDTO);
        call.enqueue(new Callback<TokenDTO>() {
            @Override
            public void onResponse(Call<TokenDTO> call, Response<TokenDTO> response) {
                if (!response.isSuccessful()) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getApplicationContext(), "Wrong credentials! Try again", Toast.LENGTH_LONG).show();
                    });
                    return;
                }
                tokenDTO = response.body();
                setUserGlobalsData();
                sendUserLoginBroadcast();
            }

            @Override
            public void onFailure(Call<TokenDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });

    }


    public void setUserGlobalsData() {
        Globals.userRole = "passenger";
//        Globals.userId = id;
        String tokenBody = JWTUtils.decode(this.tokenDTO.getAccessToken());
        try {
            Globals.userRole = JWTUtils.getUserRoleFromToken(tokenBody);
            Globals.userId = JWTUtils.getUserIdFromToken(tokenBody);
            if (Globals.userRole.equals("driver")) { setDriverActive(); }

            Call<UserReturnedDTO> call = RestUtils.userApi.doGetUser("", Globals.userId);
            call.enqueue(new Callback<UserReturnedDTO>() {

                @Override
                public void onResponse(Call<UserReturnedDTO> call, Response<UserReturnedDTO> response){
                    Globals.user = response.body();
//                    Log.d("user", Globals.user.toString());
                    Log.d("code", String.valueOf(response.code()));
                }

                @Override
                public void onFailure(Call<UserReturnedDTO> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendUserLoginBroadcast(){
        Intent intent = new Intent("userLoggedIn");
        intent.putExtra("done", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void setDriverActive() {
        Log.d("hours", LocalDateTime.now().withNano(0).toString());
        WorkingHoursStartDTO start = new WorkingHoursStartDTO(LocalDateTime.now().withNano(0).toString());
        Call<WorkingHoursDTO> call = RestUtils.driverAPI.addWorkingHours(AuthService.tokenDTO.getAccessToken(), Globals.userId, start);
        call.enqueue(new Callback<WorkingHoursDTO>() {
            @Override
            public void onResponse(Call<WorkingHoursDTO> call, Response<WorkingHoursDTO> response) {
                if (response.isSuccessful()) {
                    DriverMainActivity.workingHours = response.body();
                    Globals.isActive = true;
                    Log.d("hours", String.valueOf(response.code()));
                    Log.d("hours", DriverMainActivity.workingHours.toString());

                } else {
                    Globals.isActive = false;
                    Log.d("hours", String.valueOf(response.code()));
                    Log.d("hours", String.valueOf(response.message()));
                    Log.d("hours", String.valueOf(response));

                }
            }

            @Override
            public void onFailure(Call<WorkingHoursDTO> call, Throwable t) {
                Log.d("EVOME", t.toString());
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
