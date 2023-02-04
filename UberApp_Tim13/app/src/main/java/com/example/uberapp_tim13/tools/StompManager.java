package com.example.uberapp_tim13.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.uberapp_tim13.activities.CurrentRideActivity;
import com.example.uberapp_tim13.dialogs.ReminderDialog;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.services.RideService;

import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;

public class StompManager {
    private String LOG_TAG = "prov";
    public static StompClient stompClient;

    public void  connect() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.43.129:4321/api/socket/websocket");
        stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(this::handleConnectionLifecycle);
        stompClient.connect();
    }

    public void disconnect() {
        stompClient.disconnect();
    }

    private void handleConnectionLifecycle(LifecycleEvent event) {
        switch (event.getType()) {
            case OPENED:
                Log.d(LOG_TAG,"################## ONLINE!");
                break;
            case ERROR:
                Log.d(LOG_TAG, "################## ERROR! Trying to connect again...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Log.d(LOG_TAG, "Thread.sleep() exception.");
                }
                break;
            case CLOSED:
                Log.d(LOG_TAG, "################## OFFLINE!");
                break;
        }

    }

    @SuppressLint("CheckResult")
    public static void subscribeToScheduledRide(Context context, RideReturnedDTO ride) {
        StompManager.stompClient.topic("/topic/scheduled-ride/" + ride.getId()).subscribe(topicMessage -> {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    RideReturnedDTO newRide = Globals.gson.fromJson(topicMessage.getPayload(), RideReturnedDTO.class);
                    (new ReminderDialog(context, newRide)).show();
                }
            });
        });

        if (Globals.userRole.equals("passenger")) {
            StompManager.stompClient.topic("/topic/scheduled-ride/driver-took-off/" + ride.getId()).subscribe(topicMessage -> {
                //TODO: dodati ovde ino srdjanovo za storage
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        RideReturnedDTO newRide = Globals.gson.fromJson(topicMessage.getPayload(), RideReturnedDTO.class);
                        Intent i = new Intent(context, CurrentRideActivity.class);
                        i.putExtra("ride", newRide);
                        context.startActivity(i);
                    }
                });
            });
        }
    }
}
