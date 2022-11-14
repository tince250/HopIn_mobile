package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class DriverRideHistoryDetailsFragment extends Fragment{
    public static DriverRideHistoryDetailsFragment newInstance() {
        return new DriverRideHistoryDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_ride_history_details, container, false);
        FragmentTransition.to(RideReviewsFragment.newInstance(), getActivity(), false, R.id.listViewReviews);

        /*TextView tt = view.findViewById(R.id.textViewDistance);
        String s = getString(R.string.acceptanceRideDistance, "sss");
        tt.setText(s);*/
        view.findViewById(R.id.passengerDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(PassengerDetailsFragment.newInstance(), getActivity(), true, R.id.listRideHistory);
            }
        });
        return view;
    }
}
