package com.example.uberapp_tim13.adapters.ride_history;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {

    private Activity activity;
    List<Ride> items;

    public HistoryAdapter(Activity activity) {
        this.activity = activity;
        this.items = Mockap.getRides();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Ride ride = items.get(i);
        View view_new = view;

        if (view == null) {
            view_new = activity.getLayoutInflater().inflate(R.layout.ride_history_item, null);
        }

        ((TextView)view_new.findViewById(R.id.routeTV)).setText(ride.getPickUpLocation()+" -> "+ride.getDestination());
        String stops = "";
        for (String s : ride.getStops()){
            stops = stops + s +", ";
        }
        ((TextView)view_new.findViewById(R.id.stopsTV)).setText("Stops: "+ stops);
        ((TextView)view_new.findViewById(R.id.dateTV)).setText(ride.getStartTime());
        ((TextView)view_new.findViewById(R.id.passengersTV)).setText("Passengers: " + ride.getPassengers().size());
        ((TextView)view_new.findViewById(R.id.priceTV)).setText("Price(RSD): " + ride.getPrice());

        fitFragmentToRole(view_new, ride);

        return view_new;
    }

    private void fitFragmentToRole(View view_new, Ride ride) {
        switch (Globals.currentUser.getRole()) {
            case "driver":
                view_new.findViewById(R.id.addToFavImg).setVisibility(View.GONE);
                view_new.findViewById(R.id.driverTV).setVisibility(View.GONE);
                break;
            case "passenger":
                ((TextView)view_new.findViewById(R.id.driverTV)).setText("Driver: " + ride.getDriver().getName() + " " + ride.getDriver().getSurName());
                break;
        }
    }
}
