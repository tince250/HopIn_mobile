package com.example.uberapp_tim13.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.DriverAccountSettingsActivity;
import com.example.uberapp_tim13.DriverStatisticsActivity;
import com.example.uberapp_tim13.PassengerReportsActivity;
import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.UserLoginActivity;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.google.android.material.button.MaterialButton;

public class DriverHistoryFragment extends Fragment{
    public static DriverHistoryFragment newInstance() {
        return new DriverHistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_driver_ride_history, container, false);

        FragmentTransition.to(DriverRideHistoryFragment.newInstance(), getActivity(), false, R.id.listRideHistory);
        return view;
    }
}
