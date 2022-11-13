package com.example.uberapp_tim13.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.InboxAdapter;
import com.example.uberapp_tim13.tools.Mockap;


public class DriverInboxFragment extends Fragment {

    RecyclerView recyclerView;

    public static DriverInboxFragment newInstance() {
        return new DriverInboxFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_inbox, container, false);

        recyclerView = view.findViewById(R.id.inboxRW);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new InboxAdapter(view.getContext(), Mockap.getInboxItems()));

        return view;
    }

}