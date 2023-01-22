package com.example.uberapp_tim13.tools;

import android.util.Log;

import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;

public class StompManager {
    private String LOG_TAG = "prov";
    public static StompClient stompClient;

    public void  connect() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.43.173:4321/api/socket/websocket");
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
}
