package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;

public class RideHistoryItemFragment extends Fragment {
    public static RideHistoryItemFragment newInstance() {
        return new RideHistoryItemFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ride_history_item, container, false);
        fitFragmentToRole(view);
        view.findViewById(R.id.historyRideCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(RideHistoryDetailsFragment.newInstance(), getActivity(), true, R.id.rideHistory);
            }
        });
        return view;
    }

    private void fitFragmentToRole(View view) {
        switch (Globals.userRole) {
            case "driver":
                view.findViewById(R.id.textViewDriver).setVisibility(View.GONE);
                view.findViewById(R.id.add_to_fav).setVisibility(View.GONE);
                break;
            case "passenger":
                break;
        }
    }

}
