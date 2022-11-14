package com.example.uberapp_tim13.fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uberapp_tim13.R;

public class AccountDetailsFragment extends Fragment{
    public static AccountDetailsFragment newInstance() {
        return new AccountDetailsFragment();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_details, container, false);
        return view;
    }
}
