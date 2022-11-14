package com.example.uberapp_tim13.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.InboxAdapter;
import com.example.uberapp_tim13.items.RideHistoryAdapter;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Mockap;

public class RideHistoryFragment extends Fragment{
    RecyclerView recyclerView;
    public static RideHistoryFragment newInstance() {
        return new RideHistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_history, container, false);

        recyclerView = view.findViewById(R.id.rideHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new RideHistoryAdapter(view.getContext(), Mockap.getRides()));

        return view;
    }
}
