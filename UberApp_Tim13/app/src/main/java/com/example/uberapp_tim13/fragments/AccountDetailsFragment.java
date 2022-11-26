package com.example.uberapp_tim13.fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.ListFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.adapters.ride_history.PassengerDetailsAdapter;

/*public class AccountDetailsFragment extends ListFragment {
    public static int rideNum;
    public static AccountDetailsFragment newInstance(int r) {
        AccountDetailsFragment frag = new AccountDetailsFragment();
        rideNum = r;
        return frag;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_details, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PassengerDetailsAdapter adapter = new PassengerDetailsAdapter(getActivity(), rideNum);
        setListAdapter(adapter);
    }
}*/

public class AccountDetailsFragment extends DialogFragment {
    public static int rideNum;
    ListView listView;

    public static AccountDetailsFragment newInstance(int r) {
        AccountDetailsFragment frag = new AccountDetailsFragment();
        rideNum = r;
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_details, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        PassengerDetailsAdapter adapter = new PassengerDetailsAdapter(getActivity(), rideNum);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
