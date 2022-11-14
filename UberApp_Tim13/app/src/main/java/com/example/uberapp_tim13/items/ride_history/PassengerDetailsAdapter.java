package com.example.uberapp_tim13.items.ride_history;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.RideItem;
import com.example.uberapp_tim13.items.User;
import com.example.uberapp_tim13.tools.Mockap;

import java.util.List;

public class PassengerDetailsAdapter extends BaseAdapter {

    private Activity activity;
    List<User> users;

    public PassengerDetailsAdapter(Activity activity, int rideNum) {
        this.activity = activity;
        users = Mockap.getRides().get(rideNum).getPassengers();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        User user = users.get(i);
        View view_new = view;

        if (view == null) {
            view_new = activity.getLayoutInflater().inflate(R.layout.account_details_item, null);
        }

        ((TextView) view_new.findViewById(R.id.detailsName)).setText(user.getName());
        ((TextView) view_new.findViewById(R.id.detailsEmail)).setText(user.getEmail());
        ((TextView) view_new.findViewById(R.id.detailsContact)).setText("+381"+user.getPhone().toString());

        return view_new;
    }
}