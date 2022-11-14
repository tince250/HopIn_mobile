package com.example.uberapp_tim13;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class RideReviewsFragment extends Fragment {
    public static RideReviewsFragment newInstance() {
        return new RideReviewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_reviews, container, false);
        return view;
    }
}
