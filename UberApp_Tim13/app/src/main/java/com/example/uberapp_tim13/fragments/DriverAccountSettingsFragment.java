package com.example.uberapp_tim13.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.DriverAccountSettingsActivity;
import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.UserLoginActivity;
import com.example.uberapp_tim13.tools.Globals;

public class DriverAccountSettingsFragment extends Fragment {
    public static DriverAccountSettingsFragment newInstance() {
        return new DriverAccountSettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);
        fitFragmentToRole(view);
        setSpinner(view);

        return view;
    }

    private void setSpinner(View view) {
        Spinner areaNumSpinner = view.findViewById(R.id.spinnerAreaNumSettings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.area_number_options));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaNumSpinner.setAdapter(adapter);
    }

    private void fitFragmentToRole(View view) {
        switch (Globals.userRole) {
            case "driver":
                view.findViewById(R.id.changePayInfoBtn).setVisibility(View.GONE);
                break;
            case "passenger":
                view.findViewById(R.id.changePayInfoBtn).setVisibility(View.VISIBLE);
                break;
        }
        ((EditText) view.findViewById(R.id.editTextNameSettings)).setText(Globals.currentUser.getName());
        ((EditText) view.findViewById(R.id.editTextSurnameSettings)).setText(Globals.currentUser.getSurName());
        ((EditText) view.findViewById(R.id.editTextEmailSettings)).setText(Globals.currentUser.getEmail());
        ((EditText) view.findViewById(R.id.editTextCitySettings)).setText(Globals.currentUser.getCity());
        ((EditText) view.findViewById(R.id.editTextPhoneSettings)).setText(Globals.currentUser.getPhone());
        ((EditText) view.findViewById(R.id.editTextCCardSettings)).setText("*** **** **** " + Globals.currentUser.getCard().getNumber().split(" ")[3]);
    }
}
