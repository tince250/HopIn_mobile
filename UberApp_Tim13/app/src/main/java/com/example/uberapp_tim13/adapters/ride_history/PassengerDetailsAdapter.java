package com.example.uberapp_tim13.adapters.ride_history;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;

import java.util.List;

public class PassengerDetailsAdapter extends BaseAdapter {

    private Activity activity;
    List<UserReturnedDTO> users;

    public PassengerDetailsAdapter(Activity activity, List<UserReturnedDTO> users) {
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
        UserReturnedDTO user = users.get(i);
        View view_new = view;

        if (view == null) {
            view_new = activity.getLayoutInflater().inflate(R.layout.passenger_details_item, null);
        }

        ((TextView) view_new.findViewById(R.id.detailsNameTV)).setText(user.getName());
        ((TextView) view_new.findViewById(R.id.detailsEmailTV)).setText(user.getEmail());
        ((TextView) view_new.findViewById(R.id.detailsContactTV)).setText(user.getTelephoneNumber());

        return view_new;
    }
}