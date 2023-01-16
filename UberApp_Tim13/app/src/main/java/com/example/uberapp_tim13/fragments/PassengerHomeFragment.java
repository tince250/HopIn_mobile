package com.example.uberapp_tim13.fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dialogs.RateRideDialog;
import com.example.uberapp_tim13.dtos.LocationDTO;
import com.example.uberapp_tim13.dtos.LocationNoIdDTO;
import com.example.uberapp_tim13.dtos.RideDTO;
import com.example.uberapp_tim13.services.RideService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class PassengerHomeFragment extends Fragment {

//    private MapFragment mapFragment;
    private PlacesClient placesClient;

    public static PassengerHomeFragment newInstance() {
        return new PassengerHomeFragment();
    }
    EditText pickUpLoc;
    EditText destination;
    EditText pickUpTime;
    private LocationDTO route;

    boolean isPickup = true;

    View.OnClickListener startAutocompleteIntentListener = view -> {
        isPickup = true;
        if (view.getId() != R.id.pickUpLocET) {
            isPickup = false;
        }

        view.setOnClickListener(null);
        startAutocompleteIntent();
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mapFragment = new MapFragment();
//        getParentFragmentManager().beginTransaction().replace(R.id.map_fragment, mapFragment).commit();

        View view = inflater.inflate(R.layout.fragment_passenger_main, container, false);
        pickUpLoc = view.findViewById(R.id.pickUpLocET);
        destination = view.findViewById(R.id.destinationET);
        pickUpTime = view.findViewById(R.id.pickUpTimeET);

        // Starting new ride order, need to reinit rideInCreation
        RideService.rideInCreation = new RideDTO();


        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(view.getContext(), apiKey);
        }

        placesClient = Places.createClient(getActivity());

        pickUpLoc.setOnClickListener(startAutocompleteIntentListener);
        destination.setOnClickListener(startAutocompleteIntentListener);
        pickUpTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        pickUpTime.setText( selectedHour + ":" + selectedMinute);
                        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(selectedHour, selectedMinute, 0));
                        RideService.rideInCreation.setScheduledTime(time.toString());
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        view.findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new RateRideDialog(getActivity(), null).show();
//                if (route != null) {
//                    if (route.getDeparture() != null && route.getDestination() != null) {
//                        RideService.rideInCreation.getLocations().add(route);
//                    }else {
//                        Toast.makeText(getActivity(),"Pick locations!",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//                route = null;
//                if (RideService.rideInCreation.getScheduledTime() == null) {
//                    Toast.makeText(getActivity(),"Pick a time!",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                FragmentTransition.to(RideSettingsFragment.newInstance(), getActivity(), true, R.id.passengerFL);
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

                        Location temp = new Location(LocationManager.GPS_PROVIDER);
                        LatLng latLng = place.getLatLng();
                        temp.setLatitude(latLng.latitude);
                        temp.setLongitude(latLng.longitude);

                        String type = "pickup";
                        if (!isPickup) {
                            type = "destination";
                        }

//                        mapFragment.addMarker(temp, type);
                        fillInAddress(place);
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i("AUTO", "User canceled autocomplete");
                }
            });

    private void startAutocompleteIntent() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS_COMPONENTS,
                Place.Field.LAT_LNG, Place.Field.VIEWPORT);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("SRB")
                .setTypeFilter(TypeFilter.ADDRESS)
                .build(this.getContext());
        startAutocomplete.launch(intent);
    }

    private void fillInAddress(Place place) {
        AddressComponents components = place.getAddressComponents();
        StringBuilder address = new StringBuilder();

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

        if (route == null) {
            route = new LocationDTO();
        }
        LocationNoIdDTO loc = new LocationNoIdDTO(address.toString(), place.getLatLng().latitude,  place.getLatLng().longitude);

        if (isPickup)  {
            pickUpLoc.setText(address.toString());
            route.setDeparture(loc);
            destination.requestFocus();
        } else {
            destination.setText(address.toString());
            route.setDestinations(loc);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setBroadcastOrderedRide();
    }

    private void setBroadcastOrderedRide() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras.get("orderedRide") != null && !RideService.finished) {
                    Log.d("ORDER_FINISH", "SUCCESS");
                    Toast.makeText(getActivity(),"Ride is successfully ordered!",Toast.LENGTH_SHORT).show();
                    RideService.finished = true;
                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("orderedRide"));

    }
}