package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;

public class RideHistoryDetailsFragment extends Fragment{
    public static int rideNum;
    public static RideHistoryDetailsFragment newInstance(int num) {
        RideHistoryDetailsFragment frag = new RideHistoryDetailsFragment();
        rideNum = num;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_history_details, container, false);

        FragmentTransition.to(RideReviewsFragment.newInstance(), getActivity(), false, R.id.reviewsLV);

        Ride ride = Mockap.getRides().get(rideNum);
        fitFragmentToRole(view, ride);


        ((TextView)view.findViewById(R.id.routeTV)).setText(ride.getPickUpLocation()+" -> "+ride.getDestination());
        String stops = "";
        for (String s : ride.getStops()){
            stops = stops + s +", ";
        }
        ((TextView)view.findViewById(R.id.stopsTV)).setText("Stops: "+ stops);
        ((TextView)view.findViewById(R.id.startTimeTV)).setText(ride.getStartTime());
        ((TextView)view.findViewById(R.id.endTimeTV)).setText(ride.getEndTime());
        ((TextView)view.findViewById(R.id.passengersTV)).setText("Passengers: " + ride.getPassengers().size());
        ((TextView)view.findViewById(R.id.priceTV)).setText("Price(RSD): " + ride.getPrice());

        view.findViewById(R.id.passengerDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "sss", Toast.LENGTH_SHORT).show();
                AccountDetailsFragment dialogFragment = AccountDetailsFragment.newInstance(rideNum);
                //dialogFragment.setWidthPercent()
                dialogFragment.show(getParentFragmentManager(), "My fragment");
                //FragmentTransition.to(AccountDetailsFragment.newInstance(rideNum), getActivity(), true, R.id.driverFL);
            }
        });
        /*TextView tt = view.findViewById(R.id.textViewDistance);
        String s = getString(R.string.acceptanceRideDistance, "sss");
        tt.setText(s);*/
//        view.findViewById(R.id.passengerDetails).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentTransition.to(PassengerDetailsFragment.newInstance(), getActivity(), true, R.id.list);
//            }
//        });
        return view;
    }

    private void fitFragmentToRole(View view, Ride ride) {
        switch (Globals.userRole) {
            case "driver":
                view.findViewById(R.id.driver_info_card_hist).setVisibility(View.GONE);
                break;
            case "passenger":
                ((TextView)view.findViewById(R.id.nameDriverTV)).setText("Driver: " + ride.getDriver().getName() + " " + ride.getDriver().getSurName());
                break;
        }
    }
}
