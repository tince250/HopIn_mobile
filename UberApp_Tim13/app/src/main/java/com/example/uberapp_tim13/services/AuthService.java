package com.example.uberapp_tim13.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.uberapp_tim13.activities.AcceptanceRideActivity;
import com.example.uberapp_tim13.activities.PassengerMainActivity;
import com.example.uberapp_tim13.dtos.CredentialsDTO;
import com.example.uberapp_tim13.dtos.RideInviteDTO;
import com.example.uberapp_tim13.dtos.TokenDTO;
import com.example.uberapp_tim13.dtos.UserDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.JWTUtils;
import com.example.uberapp_tim13.tools.StompManager;

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
    int id;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        CredentialsDTO credentialsDTO = (CredentialsDTO) extras.get("credentials");
        id = extras.getInt("id");
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
        Log.d("prov", "login usao");
//        Call<TokenDTO> call = RestUtils.userApi.login(credentialsDTO);
//        call.enqueue(new Callback<TokenDTO>() {
//            @Override
//            public void onResponse(Call<TokenDTO> call, Response<TokenDTO> response) {
//                Log.d("prov", String.valueOf(response.code()));
//                tokenDTO = response.body();
//                setUserGlobalsData();
//                connectToSocket();
//                sendUserLoginBroadcast();
//            }
//
//            @Override
//            public void onFailure(Call<TokenDTO> call, Throwable t) {
//                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
//            }
//        });



        setUserGlobalsData();
        connectToSocket();
        sendUserLoginBroadcast();
    }

    private void connectToSocket() {
        StompManager manager = new StompManager();
        manager.connect();

        if (Globals.userRole == "passenger")
            manager.stompClient.topic("/topic/invites/" + Globals.userId).subscribe(topicMessage -> {
                        RideInviteDTO invite = Globals.gson.fromJson(topicMessage.getPayload(), RideInviteDTO.class);
                        Intent i = new Intent(getApplicationContext(), AcceptanceRideActivity.class);
                        i.putExtra("invite", invite);
                        i.putExtra("type", "invite");
                        startActivity(i);
                    }
            );
        else {
            if (Globals.userRole == "driver") {
                //TODO: sub to receive ride orders
            }

        }
    }

    public void setUserGlobalsData() {
        Globals.userRole = "passenger";
        Globals.userId = id;
//        String tokenBody = JWTUtils.decode(this.tokenDTO.getAccessToken());
//        try {
////            Globals.userRole = JWTUtils.getUserRoleFromToken(tokenBody);
////            Globals.userId = JWTUtils.getUserIdFromToken(tokenBody);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void sendUserLoginBroadcast(){
        Intent intent = new Intent("userLoggedIn");
        intent.putExtra("done", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
