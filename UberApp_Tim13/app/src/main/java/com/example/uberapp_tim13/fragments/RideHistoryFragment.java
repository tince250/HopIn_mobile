package com.example.uberapp_tim13.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.RideItem;
import com.example.uberapp_tim13.items.ride_history.HistoryAdapter;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.Mockap;

public class RideHistoryFragment extends ListFragment{
    public static RideHistoryFragment newInstance() {
        return new RideHistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_history, container, false);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (Globals.userRole == "passenger") {
            FragmentTransition.to(RideHistoryDetailsFragment.newInstance(position), getActivity(), true, R.id.passenger_fl);
        } else {
            FragmentTransition.to(RideHistoryDetailsFragment.newInstance(position), getActivity(), true, R.id.driver_fl);

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HistoryAdapter adapter = new HistoryAdapter(getActivity());
        setListAdapter(adapter);
    }


}
