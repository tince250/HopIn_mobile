package com.example.uberapp_tim13.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.RideItem;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;

public class RideHistoryDetailsFragment extends Fragment{
    public static RideHistoryDetailsFragment newInstance(int ride_num) {
        RideHistoryDetailsFragment frag = new RideHistoryDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("ride_id", ride_num);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_history_details, container, false);

        FragmentTransition.to(RideReviewsFragment.newInstance(), getActivity(), false, R.id.listViewReviews);

        RideItem ride = Mockap.getRides().get(getArguments().getInt("ride_id"));
//        fitFragmentToRole(view, ride);


        ((TextView)view.findViewById(R.id.textViewRoute)).setText(ride.getPickUpLocation()+" -> "+ride.getDestination());
        String stops = "";
        for (String s : ride.getStops()){
            stops = stops + s +", ";
        }
        ((TextView)view.findViewById(R.id.textViewStops)).setText("Stops: "+ stops);
        ((TextView)view.findViewById(R.id.textViewStartTime)).setText(ride.getStartTime());
        ((TextView)view.findViewById(R.id.textViewEndTime)).setText(ride.getEndTime());
        ((TextView)view.findViewById(R.id.textViewPassengers)).setText("Passengers: " + ride.getPassengers().size());
        ((TextView)view.findViewById(R.id.textViewPrice)).setText("Price(RSD): " + ride.getPrice());

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

    private void fitFragmentToRole(View view, RideItem ride) {
        switch (Globals.userRole) {
            case "driver":
                view.findViewById(R.id.driver_info_card_hist).setVisibility(View.GONE);
                break;
            case "passenger":
                ((TextView)view.findViewById(R.id.driver_info_card_hist)).setText("Driver: " + ride.getDriver().getName() + " " + ride.getDriver().getSurName());
                break;
        }
    }
}
