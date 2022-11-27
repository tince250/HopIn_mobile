package com.example.uberapp_tim13.fragments;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.AccountSettingsActivity;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class PassengerHomeFragment extends Fragment {

    public static PassengerHomeFragment newInstance() {
        return new PassengerHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passenger_main, container, false);

        view.findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(RideSettingsFragment.newInstance(), getActivity(), true, R.id.passengerFL);
            }
        });

        setTimePicker(view);
        return view;

    }

    private void setTimePicker(View view) {
        MaterialButton pickUpTimeBtn = view.findViewById(R.id.pickUpTimeBtn);
        EditText pickUpTimeET = view.findViewById(R.id.pickUpTimeET);
        pickUpTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.TimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                pickUpTimeET.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }
}
