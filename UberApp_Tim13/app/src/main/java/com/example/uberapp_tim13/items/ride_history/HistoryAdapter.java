package com.example.uberapp_tim13.items.ride_history;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.RideItem;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {

    private Activity activity;
    List<RideItem> items;

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
        RideItem ride = items.get(i);
        View view_new = view;

        if (view == null) {
            view_new = activity.getLayoutInflater().inflate(R.layout.ride_history_item, null);
        }

        ((TextView)view_new.findViewById(R.id.textViewRoute)).setText(ride.getPickUpLocation()+" -> "+ride.getDestination());
        String stops = "";
        for (String s : ride.getStops()){
            stops = stops + s +", ";
        }
        ((TextView)view_new.findViewById(R.id.textViewStops)).setText("Stops: "+ stops);
        ((TextView)view_new.findViewById(R.id.textViewDate)).setText(ride.getStartTime());
        ((TextView)view_new.findViewById(R.id.textViewPassengers)).setText("Passengers: " + ride.getPassengers().size());
        ((TextView)view_new.findViewById(R.id.textViewPrice)).setText("Price(RSD): " + ride.getPrice());

        fitFragmentToRole(view_new, ride);

        return view_new;
    }

    private void fitFragmentToRole(View view_new, RideItem ride) {
        switch (Globals.currentUser.getRole()) {
            case "driver":
                view_new.findViewById(R.id.add_to_fav).setVisibility(View.GONE);
                view_new.findViewById(R.id.textViewDriver).setVisibility(View.GONE);
                break;
            case "passenger":
                ((TextView)view_new.findViewById(R.id.textViewDriver)).setText("Driver: " + ride.getDriver().getName() + " " + ride.getDriver().getSurName());
                break;
        }
    }
}
