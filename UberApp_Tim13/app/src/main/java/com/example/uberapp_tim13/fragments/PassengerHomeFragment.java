package com.example.uberapp_tim13.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class PassengerHomeFragment extends Fragment {

    public static PassengerHomeFragment newInstance() {
        return new PassengerHomeFragment();
    }
    EditText pickUpLoc;
    EditText destination;

    View.OnClickListener startAutocompleteIntentListener = view -> {
        view.setOnClickListener(null);
        startAutocompleteIntent();
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passenger_main, container, false);
        pickUpLoc = view.findViewById(R.id.pickUpLocET);
        destination = view.findViewById(R.id.destinationET);

        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(view.getContext(), apiKey);
        }

        pickUpLoc.setOnClickListener(startAutocompleteIntentListener);

        view.findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(RideSettingsFragment.newInstance(), getActivity(), true, R.id.passengerFL);
            }
        });

        return view;
    }

    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            (ActivityResultCallback<ActivityResult>) result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);

                        // Write a method to read the address components from the Place
                        // and populate the form with the address components
                        Log.d("AUTO", "Place: " + place.getAddressComponents());
//                        fillInAddress(place);
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i("AUTO", "User canceled autocomplete");
                }
            });

    private void startAutocompleteIntent() {

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS_COMPONENTS,
                Place.Field.LAT_LNG, Place.Field.VIEWPORT);

        // Build the autocomplete intent with field, country, and type filters applied
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("SRB")
                .setTypeFilter(TypeFilter.ADDRESS)
                .build(this.getContext());
        startAutocomplete.launch(intent);
    }

    private void fillInAddress(Place place) {
        AddressComponents components = place.getAddressComponents();
        StringBuilder address = new StringBuilder();

        // Get each component of the address from the place details,
        // and then fill-in the corresponding field on the form.
        // Possible AddressComponent types are documented at https://goo.gle/32SJPM1
        if (components != null) {
            for (AddressComponent component : components.asList()) {
                String type = component.getTypes().get(0);
                switch (type) {
                    case "street_number": {
                        address.insert(0, component.getName());
                        break;
                    }

                    case "route": {
                        address.append(" ");
                        address.append(component.getShortName());
                        break;
                    }
                }
            }
        }

        pickUpLoc.setText(address.toString());

        // After filling the form with address components from the Autocomplete
        // prediction, set cursor focus on the second address line to encourage
        // entry of sub-premise information such as apartment, unit, or floor number.
        destination.requestFocus();

        // Add a map for visual confirmation of the address
//        showMap(place);
    }
}
