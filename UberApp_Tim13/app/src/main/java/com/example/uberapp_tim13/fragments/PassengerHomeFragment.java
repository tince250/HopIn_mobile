package com.example.uberapp_tim13.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.AccountSettingsActivity;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class PassengerHomeFragment extends Fragment {

    public static PassengerHomeFragment newInstance() {
        return new PassengerHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passenger_main, container, false);

        view.findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(RideSettingsFragment.newInstance(), getActivity(), true, R.id.passengerFL);
            }
        });

        return view;
    }
}
