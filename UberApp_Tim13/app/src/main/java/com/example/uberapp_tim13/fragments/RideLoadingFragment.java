package com.example.uberapp_tim13.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.CurrentRideActivity;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.StompManager;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class RideLoadingFragment extends Fragment {

    public static RideLoadingFragment newInstance() {
        return new RideLoadingFragment();
    }

    private ProgressBar bar;
    private TextView title;
    private StompClient mStompClient;

    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_loading, container, false);
        bar = (ProgressBar) view.findViewById(R.id.progressBar);
        title = view.findViewById(R.id.loadTitle);
        bar.setVisibility(View.VISIBLE);
        Intent intentRideService = new Intent(getContext(), RideService.class);
        intentRideService.putExtra("method", "orderRide");
        requireActivity().startService(intentRideService);
        setBroadcastOrderedRide();
        return view;
    }

    private void setBroadcastOrderedRide() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (RideService.returnedRide != null && !RideService.finished) {
                    Log.d("ORDER_FINISH", "SUCCESS");
                    StompManager.stompClient.topic("/topic/ride-offer-response/" + Globals.userId).subscribe(topicMessage -> {
                        Log.d("ORDER_FINISH", topicMessage.getPayload());
                        if (topicMessage.getPayload().trim().equals("true")) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    Log.d("ORDER_FINISH", "ACCEPT");
                                    Intent i = new Intent(getActivity(), CurrentRideActivity.class);
                                    i.putExtra("ride", RideService.returnedRide);
                                    startActivity(i);
                                }
                            });

                        } else {
                            // handles both the decline and no suitable driver case
                            Log.d("ORDER_FINISH", "DECLINE");
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    bar.setVisibility(View.GONE);
                                    title.setText("We're not currently able to find u a ride :( \n Try to Hop In later!");
                                }
                            });
                        }
                    });
                    Toast.makeText(getActivity(),"Ride is successfully ordered!",Toast.LENGTH_SHORT).show();
                    RideService.finished = true;
                } else {
                    Toast.makeText(getActivity(),"Ups, something went wrong!",Toast.LENGTH_SHORT).show();
                    RideService.finished = true;
                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("orderedRide"));

    }

}

