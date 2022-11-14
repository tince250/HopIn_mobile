package com.example.uberapp_tim13.fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.tools.FragmentTransition;

public class PassengerDetailsFragment extends Fragment {
    public static PassengerDetailsFragment newInstance() {
        return new PassengerDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_details, container, false);
//        getActivity().setTitle("Passenger info");
        FragmentTransition.to(AccountDetailsFragment.newInstance(), getActivity(), false, R.id.listViewAccountDetails);
        return view;
    }
}
