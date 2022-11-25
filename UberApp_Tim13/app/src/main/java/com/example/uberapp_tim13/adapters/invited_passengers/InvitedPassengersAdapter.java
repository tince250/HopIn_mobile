package com.example.uberapp_tim13.adapters.invited_passengers;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.model.User;

import java.util.ArrayList;
import java.util.List;

public class InvitedPassengersAdapter extends BaseAdapter {

    private Activity activity;
    private List<User> users;

    public InvitedPassengersAdapter(Activity activity, List<User> users) {
        this.activity = activity;
        this.users = users;
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
        User user = this.users.get(i);
        View view_new = view;

        if(view == null) {
            view_new = activity.getLayoutInflater().inflate(R.layout.invited_passenger_item, null);
        }

        ((TextView)view_new.findViewById(R.id.passengerNameTV)).setText(user.getName() + " " + user.getSurName());

        return view_new;
    }
}
