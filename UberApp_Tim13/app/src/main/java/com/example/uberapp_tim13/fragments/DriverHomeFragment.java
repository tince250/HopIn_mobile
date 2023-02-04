package com.example.uberapp_tim13.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.CurrentRideActivity;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.Globals;
import com.google.android.material.button.MaterialButton;

public class DriverHomeFragment extends Fragment {

    private MapFragment mapFragment;
    public static DriverHomeFragment newInstance() {
        return new DriverHomeFragment();
    }
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver_home, container, false);
        mapFragment = new MapFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.map_container_aroundyou, mapFragment).commit();
        //mapFragment.pozovi();

        view.findViewById(R.id.currentRideBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CurrentRideActivity.class);
                i.putExtra("ride", Globals.currentRide);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Globals.currentRide == null) {
            view.findViewById(R.id.currentRideBtn).setEnabled(false);
        } else {
            view.findViewById(R.id.currentRideBtn).setEnabled(true);
        }
    }
}
