package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class RideSettingsFragment extends Fragment {

    public static RideSettingsFragment newInstance() {
        return new RideSettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_settings, container, false);

        view.findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(PassengerHomeFragment.newInstance(), getActivity(), true, R.id.passengerFL);
            }
        });

        view.findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(InviteOthersFragment.newInstance(), getActivity(), true, R.id.passengerFL);
            }
        });

        return view;
    }
}
