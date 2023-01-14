package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dialogs.PassengerDetailsDialog;
import com.example.uberapp_tim13.dialogs.DriverDetailsDialog;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;

public class RideHistoryDetailsFragment extends Fragment{
    public static RideReturnedDTO ride;
    public static RideHistoryDetailsFragment newInstance(RideReturnedDTO r) {
        RideHistoryDetailsFragment frag = new RideHistoryDetailsFragment();
        ride = r;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_history_details, container, false);

        FragmentTransition.to(RideReviewsFragment.newInstance(), getActivity(), false, R.id.reviewsLV);
        fitFragmentToRole(view, ride);


        ((TextView)view.findViewById(R.id.routeTV)).setText(ride.getRouteDepartureDestinationTitle());
        ((TextView)view.findViewById(R.id.startTimeTV)).setText(ride.getStartDateTextView());
        ((TextView)view.findViewById(R.id.endTimeTV)).setText(ride.getStartDateTextView());
        ((TextView)view.findViewById(R.id.passengersTV)).setText("Passengers: " + ride.getPassengers().size());
        ((TextView)view.findViewById(R.id.priceTV)).setText("Price(RSD): " + ride.getTotalCost());
        //distanca je losa
        
        view.findViewById(R.id.passengerDetailsRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "sss", Toast.LENGTH_SHORT).show();
                //AccountDetailsFragment dialogFragment = AccountDetailsFragment.newInstance(rideNum);
                //dialogFragment.setWidthPercent()
               // dialogFragment.show(getParentFragmentManager(), "My fragment");
                new PassengerDetailsDialog(getActivity(), 1).show();
                //FragmentTransition.to(AccountDetailsFragment.newInstance(rideNum), getActivity(), true, R.id.driverFL);
            }
        });

        view.findViewById(R.id.driverDetailsRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DriverDetailsDialog(getActivity(), 1).show();
                //DriverDetailsDialog driverDetailsDialog = DriverDetailsDialog.newInstance(rideNum);
                //driverDetailsDialog.show(getParentFragmentManager(), "My fragment");
            }
        });
        return view;
    }

    private void fitFragmentToRole(View view, RideReturnedDTO ride) {
        switch (Globals.userRole) {
            case "driver":
                view.findViewById(R.id.driver_info_card_hist).setVisibility(View.GONE);
                break;
            case "passenger":
                ((TextView)view.findViewById(R.id.nameDriverTV)).setText("Driver: " + ride.getDriver().getEmail());
                break;
        }
    }
}
