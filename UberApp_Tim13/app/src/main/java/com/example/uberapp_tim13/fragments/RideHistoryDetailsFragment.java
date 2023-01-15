package com.example.uberapp_tim13.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.ride_history.HistoryAdapter;
import com.example.uberapp_tim13.adapters.ride_history.RatingsAdapter;
import com.example.uberapp_tim13.dialogs.PassengerDetailsDialog;
import com.example.uberapp_tim13.dialogs.DriverDetailsDialog;
import com.example.uberapp_tim13.dtos.reviews.CompleteRideReviewDTO;
import com.example.uberapp_tim13.dtos.reviews.ReviewReturnedDTO;
import com.example.uberapp_tim13.dtos.rides.AllPassengerRidesDTO;
import com.example.uberapp_tim13.dtos.rides.RideReturnedDTO;
import com.example.uberapp_tim13.services.DriverService;
import com.example.uberapp_tim13.services.ReviewService;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;

import java.util.ArrayList;
import java.util.List;

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

        //FragmentTransition.to(RideReviewsFragment.newInstance(), getActivity(), false, R.id.reviewsLV);

        fitFragmentToRole(view, ride);

        ((TextView)view.findViewById(R.id.routeTV)).setText(ride.getRouteDepartureDestinationTitle());
        ((TextView)view.findViewById(R.id.startTimeTV)).setText(ride.getStartDateTextView());
        ((TextView)view.findViewById(R.id.endTimeTV)).setText(ride.getStartDateTextView());
        ((TextView)view.findViewById(R.id.passengersTV)).setText("Passengers: " + ride.getPassengers().size());
        ((TextView)view.findViewById(R.id.priceTV)).setText("Price(RSD): " + ride.getTotalCost());

        setListViewAdapterAndRating(view, ride.getId());
        //distanca je losa

        view.findViewById(R.id.passengerDetailsRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {;
                new PassengerDetailsDialog(getActivity(), ride).show();
            }
        });

        view.findViewById(R.id.driverDetailsRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DriverDetailsDialog(getActivity(), ride).show();
            }
        });
        return view;
    }

    private void setListViewAdapterAndRating(View view, int rideId){
        Intent intent = new Intent(getContext(), ReviewService.class);
        intent.putExtra("method", "getAllReviews");
        intent.putExtra("rideId", 3);
        requireActivity().startService(intent);


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                ArrayList<CompleteRideReviewDTO> completeReviews = (ArrayList<CompleteRideReviewDTO>) extras.get("allReviews");
                ArrayList<ReviewReturnedDTO> allReviews = new ArrayList<ReviewReturnedDTO>();
                for (CompleteRideReviewDTO completeReview : completeReviews){
                    if (completeReview.getDriverReview() != null)
                        allReviews.add(completeReview.getDriverReview());
                    if (completeReview.getVehicleReview() != null)
                        allReviews.add(completeReview.getVehicleReview());
                }

                RatingsAdapter adapter = new RatingsAdapter(getActivity(), allReviews);
                ListView listView = view.findViewById(R.id.list);
                listView.setAdapter(adapter);

                ((RatingBar)view.findViewById(R.id.ratingBar)).setRating((Float) extras.get("allRideReviewsRating"));
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("rideHistoryDetailsFragment"));

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
