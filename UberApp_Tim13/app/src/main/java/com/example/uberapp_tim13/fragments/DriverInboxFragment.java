package com.example.uberapp_tim13.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uberapp_tim13.ChatActivity;
import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.items.InboxAdapter;
import com.example.uberapp_tim13.tools.FragmentTransition;
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

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        startActivity(new Intent(getActivity(), ChatActivity.class));
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        setSpinner(view);
        return view;
    }

    private void setSpinner(View view) {
        Spinner inboxSpinner = view.findViewById(R.id.spinner_inbox);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, view.getResources().getStringArray(R.array.inbox_filter));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inboxSpinner.setAdapter(adapter);
    }

}