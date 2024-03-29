package com.example.uberapp_tim13.fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.ride_history.PassengerDetailsAdapter;

public class AccountDetailsFragment extends DialogFragment {
    public static int rideNum;
    ListView listView;

    public static AccountDetailsFragment newInstance(int r) {
        AccountDetailsFragment fragment = new AccountDetailsFragment();
        rideNum = r;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_passenger_details, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        //assengerDetailsAdapter adapter = new PassengerDetailsAdapter(getActivity(), rideNum);
        //listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        //params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
