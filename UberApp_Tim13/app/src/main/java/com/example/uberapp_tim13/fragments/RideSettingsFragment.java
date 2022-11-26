package com.example.uberapp_tim13.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.google.android.material.button.MaterialButton;

public class RideSettingsFragment extends Fragment {

    public static RideSettingsFragment newInstance() {
        return new RideSettingsFragment();
    }

    private boolean babyClicked = false;
    private boolean petClicked = false;
    private MaterialButton basicCarBtn;
    private MaterialButton vanBtn;
    private MaterialButton luxuryCarBtn;
    private MaterialButton babyBtn;
    private MaterialButton petBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_settings, container, false);

        basicCarBtn = (MaterialButton)view.findViewById(R.id.basicCarBtn);
        vanBtn = (MaterialButton)view.findViewById(R.id.vanBtn);
        luxuryCarBtn = (MaterialButton)view.findViewById(R.id.luxuryCarBtn);
        babyBtn = (MaterialButton)view.findViewById(R.id.babyBtn);
        petBtn = (MaterialButton)view.findViewById(R.id.petBtn);

        getActivity().setTitle("Ride settings");

        view.findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(PassengerHomeFragment.newInstance(), getActivity(), true, R.id.passengerFL);
            }
        });

        view.findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(InviteOthersFragment.newInstance(), getActivity(), true, R.id.passengerFL);
            }
        });
        
        vehicleTypeBtnsSetUp(view);
        babyPetBtnsSetUp(view);

        return view;
    }


    private void vehicleTypeBtnsSetUp(View view) {
        basicCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBtnToBlue(basicCarBtn);
                changeBtnToGray(vanBtn);
                changeBtnToGray(luxuryCarBtn);
            }
        });

        view.findViewById(R.id.vanBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBtnToBlue(vanBtn);
                changeBtnToGray(basicCarBtn);
                changeBtnToGray(luxuryCarBtn);
            }
        });

        view.findViewById(R.id.luxuryCarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBtnToBlue(luxuryCarBtn);
                changeBtnToGray(vanBtn);
                changeBtnToGray(basicCarBtn);
            }
        });
    }

    private void babyPetBtnsSetUp(View view) {
        view.findViewById(R.id.babyBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (babyClicked) {
                    changeBtnToGray(babyBtn);
                }else {
                    changeBtnToBlue(babyBtn);
                }
                babyClicked = !babyClicked;
            }
        });

        view.findViewById(R.id.petBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (petClicked) {
                    changeBtnToGray(petBtn);
                }else {
                    changeBtnToBlue(petBtn);
                }
                petClicked = !petClicked;
            }
        });
    }

    private void changeBtnToBlue(MaterialButton btn) {
        btn.setTextColor(getResources().getColor(R.color.lighter_blue));
        btn.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.lighter_blue)));
        btn.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.lighter_blue)));
    }

    private void changeBtnToGray(MaterialButton btn) {
        btn.setTextColor(getResources().getColor(R.color.disabled_gray));
        btn.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.disabled_gray)));
        btn.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.disabled_gray)));
    }
}
