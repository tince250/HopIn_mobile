package com.example.uberapp_tim13.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.FragmentTransition;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class RideLoadingFragment extends Fragment {

    public static RideLoadingFragment newInstance() {
        return new RideLoadingFragment();
    }

    private ProgressBar bar;
    private StompClient mStompClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_loading, container, false);
        bar = (ProgressBar) view.findViewById(R.id.progressBar);
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
                if (extras.get("orderedRide") != null && !RideService.finished) {
                    Log.d("ORDER_FINISH", "SUCCESS");
                    Toast.makeText(getActivity(),"Ride is successfully ordered!",Toast.LENGTH_SHORT).show();
                    RideService.finished = true;
                    FragmentTransition.to(PassengerHomeFragment.newInstance(), getActivity(), true, R.id.passengerFL);
                } else {
                    Toast.makeText(getActivity(),"Ups, something went wrong!",Toast.LENGTH_SHORT).show();
                    RideService.finished = true;
                    FragmentTransition.to(PassengerHomeFragment.newInstance(), getActivity(), true, R.id.passengerFL);
                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("orderedRide"));

    }

}

