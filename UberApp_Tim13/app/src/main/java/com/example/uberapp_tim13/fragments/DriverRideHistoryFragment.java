package com.example.uberapp_tim13.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.DriverAccountSettingsActivity;
import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class DriverRideHistoryFragment extends Fragment {
    public static DriverRideHistoryFragment newInstance() {
        return new DriverRideHistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_history_list, container, false);

        view.findViewById(R.id.historyRideCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(DriverRideHistoryDetailsFragment.newInstance(), getActivity(), true, R.id.listRideHistory);
            }
        });
        return view;
    }

}
