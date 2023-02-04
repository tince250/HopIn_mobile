package com.example.uberapp_tim13.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.ChatActivity;
import com.example.uberapp_tim13.activities.CurrentRideActivity;
import com.example.uberapp_tim13.adapters.ride_history.RatingsAdapter;
import com.example.uberapp_tim13.dialogs.PassengerDetailsDialog;
import com.example.uberapp_tim13.dialogs.DriverDetailsDialog;
import com.example.uberapp_tim13.dtos.CompleteRideReviewDTO;
import com.example.uberapp_tim13.dtos.InboxReturnedDTO;
import com.example.uberapp_tim13.dtos.ReviewReturnedDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.services.ReviewService;
import com.example.uberapp_tim13.tools.Globals;

import com.example.uberapp_tim13.tools.Mockap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RideHistoryDetailsFragment extends Fragment{
    public static RideReturnedDTO ride;
    private MapFragment mapFragment;

    public static RideHistoryDetailsFragment newInstance(RideReturnedDTO r) {
        RideHistoryDetailsFragment frag = new RideHistoryDetailsFragment();
        ride = r;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_history_details, container, false);

        getParentFragmentManager().beginTransaction().replace(R.id.map_fragment, new MapFragment(ride)).commit();
        fitFragmentToRole(view, ride);

        ((TextView)view.findViewById(R.id.routeTV)).setText(ride.getRouteDepartureDestinationTitle());
        ((TextView)view.findViewById(R.id.startTimeTV)).setText(ride.getStartDateTextView());
        ((TextView)view.findViewById(R.id.endTimeTV)).setText(ride.getEndDateTextView());
        ((TextView)view.findViewById(R.id.passengersTV)).setText("Passengers: " + ride.getPassengers().size());
        ((TextView)view.findViewById(R.id.priceTV)).setText("Price: " + ride.getTotalCost() + "RSD");
        ((TextView)view.findViewById(R.id.distanceTV)).setText("Distance: " + ride.getDistance() + "km");

        setListViewAdapterAndRating(view, ride.getId());

        if (Globals.userRole.equals("passenger") && ride.getPassengers().size() == 1) {
            ((TextView)view.findViewById(R.id.displayMessageTV)).setText("Only you");
        } else {
            view.findViewById(R.id.passengerDetailsRL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PassengerDetailsDialog(getActivity(), ride).show();
                }
            });
        }


        view.findViewById(R.id.driverDetailsRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DriverDetailsDialog(getActivity(), ride).show();
            }
        });

        view.findViewById(R.id.chatBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int otherId = Globals.user.getId() == ride.getDriver().getId()? ride.getPassengers().get(0).getId(): ride.getDriver().getId();
                Call<InboxReturnedDTO> call = RestUtils.userApi.getRideInbox(AuthService.tokenDTO.getAccessToken(), otherId, ride.getId());
                call.enqueue(new Callback<InboxReturnedDTO>() {
                    @Override
                    public void onResponse(Call<InboxReturnedDTO> call, Response<InboxReturnedDTO> response) {
                        if (response.isSuccessful()) {
                            Intent i = new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("inbox", response.body());
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<InboxReturnedDTO> call, Throwable t) {
                        Log.d("ERROR", "Error fetching inbox");
                    }
                });
            }
        });

        return view;
    }

    private void setListViewAdapterAndRating(View view, int rideId){
        Intent intent = new Intent(getContext(), ReviewService.class);
        intent.putExtra("method", "getAllReviews");
        intent.putExtra("rideId", rideId);
        requireActivity().startService(intent);


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                ArrayList<CompleteRideReviewDTO> completeReviews = (ArrayList<CompleteRideReviewDTO>) extras.get("allReviews");

                if (completeReviews.size() == 0) {
                    view.findViewById(R.id.notRatedTV).setVisibility(View.VISIBLE);
                    return;
                }

                setListViewListener(view);
                completeReviews = sortReviews(completeReviews);


                RatingsAdapter adapter = new RatingsAdapter(getActivity(), completeReviews);
                ListView listView = view.findViewById(R.id.list);
                listView.setAdapter(adapter);

                ((RatingBar)view.findViewById(R.id.ratingBar)).setRating((Float) extras.get("allRideReviewsRating"));
                view.findViewById(R.id.ratingBar).setVisibility(View.VISIBLE);
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("rideHistoryDetailsFragment"));

    }

    private ArrayList<CompleteRideReviewDTO> sortReviews(ArrayList<CompleteRideReviewDTO> completeReviews) {
        List<CompleteRideReviewDTO> reviewList = completeReviews.stream().filter((review) -> {return review.getDriverReview().getPassenger().getId() == Globals.user.getId();}).collect(Collectors.toList());
        if (reviewList.size() == 0)
            return completeReviews;

        CompleteRideReviewDTO review = reviewList.get(0);
        completeReviews.remove(review);
        completeReviews.add(0, review);

        return completeReviews;
    }

    private void setListViewListener(View view) {
        ListView lv = (ListView)view.findViewById(R.id.list);
        lv.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
    }

    private void fitFragmentToRole(View view, RideReturnedDTO ride) {
        switch (Globals.userRole) {
            case "driver":
                view.findViewById(R.id.driver_info_card_hist).setVisibility(View.GONE);
                break;
            case "passenger":
                ((TextView)view.findViewById(R.id.nameDriverTV)).setText("Driver");
                break;
        }
    }
}
