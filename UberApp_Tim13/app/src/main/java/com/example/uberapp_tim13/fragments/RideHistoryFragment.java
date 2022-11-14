package com.example.uberapp_tim13.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class RideHistoryFragment extends Fragment{
    public static RideHistoryFragment newInstance() {
        return new RideHistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_history, container, false);

        FragmentTransition.to(RideHistoryItemFragment.newInstance(), getActivity(), false, R.id.rideHistory);
        return view;
    }
}
