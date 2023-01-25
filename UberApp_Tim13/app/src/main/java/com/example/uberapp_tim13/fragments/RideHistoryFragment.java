package com.example.uberapp_tim13.fragments;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.ride_history.HistoryAdapter;
import com.example.uberapp_tim13.dtos.AllPassengerRidesDTO;
import com.example.uberapp_tim13.services.DriverService;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;

public class RideHistoryFragment extends ListFragment{
    public static RideHistoryFragment newInstance() {
        return new RideHistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_history, container, false);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("rola", Globals.userRole );
        if (Globals.userRole.equals("passenger")) {
            FragmentTransition.to(RideHistoryDetailsFragment.newInstance(allRides.getResults().get(position)), getActivity(), true, R.id.passengerFL);
        } else {
            Log.d("udje", "kako");
            FragmentTransition.to(RideHistoryDetailsFragment.newInstance(allRides.getResults().get(position)), getActivity(), true, R.id.driverFL);

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getContext(), RideService.class);
        intent.putExtra("method", "getAllRides");
        intent.putExtra("userId", Globals.userId);
        intent.putExtra("role", Globals.userRole);
        requireActivity().startService(intent);


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                allRides = (AllPassengerRidesDTO) extras.get("allRides");

                HistoryAdapter adapter = new HistoryAdapter(allRides.getResults(), getActivity());
                setListAdapter(adapter);
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("rideHistoryFragment"));

    }

    private AllPassengerRidesDTO allRides;
}
