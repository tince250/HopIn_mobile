package com.example.uberapp_tim13;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class DriverRideHistoryDetailsFragment extends Fragment{
    public static DriverRideHistoryDetailsFragment newInstance() {
        return new DriverRideHistoryDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_ride_history_details, container, false);

        return view;
    }
}
