package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.uberapp_tim13.dtos.CredentialsDTO;
import com.example.uberapp_tim13.dtos.TokenDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.JWTUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;

public class AuthService extends Service {

    public static TokenDTO tokenDTO;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        CredentialsDTO credentialsDTO = (CredentialsDTO) extras.get("credentials");
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
                tokenDTO = response.body();
                setUserGlobalsData();
                connectToSocket();
            }

            @Override
            public void onFailure(Call<TokenDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    private void connectToSocket() {
//        Globals.stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:4321/api/socket/websocket");
//        Globals.stompClient.connect();
//        Globals.stompClient.topic("/topic/invites/" + Globals.userId).subscribe(topicMessage -> {
//            //TODO: display invitation
//            //TODO: gde nam je logout
//        });
    }

    public void setUserGlobalsData() {
        String tokenBody = JWTUtils.decode(this.tokenDTO.getAccessToken());
        try {
            Globals.userRole = JWTUtils.getUserRoleFromToken(tokenBody);
            Globals.userId = JWTUtils.getUserIdFromToken(tokenBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
