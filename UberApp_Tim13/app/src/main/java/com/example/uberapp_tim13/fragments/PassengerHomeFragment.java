package com.example.uberapp_tim13.fragments;

import static com.google.maps.android.Context.getApplicationContext;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.activities.CurrentRideActivity;
import com.example.uberapp_tim13.activities.ReportsActivity;
import com.example.uberapp_tim13.dialogs.RateRideDialog;
import com.example.uberapp_tim13.dtos.LocationDTO;
import com.example.uberapp_tim13.dtos.LocationNoIdDTO;
import com.example.uberapp_tim13.dtos.RideDTO;
import com.example.uberapp_tim13.dtos.RideForReportDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.StompManager;
import com.example.uberapp_tim13.tools.Utils;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;


public class PassengerHomeFragment extends Fragment {


    private MapFragment mapFragment;
    private PlacesClient placesClient;

    private StompClient mStompClient;

    private boolean orderAgain;

    private boolean alreadyInRide = false;


    public static PassengerHomeFragment newInstance() {
        return new PassengerHomeFragment();
    }

    EditText pickUpLoc;
    EditText destination;
    EditText pickUpTime;
    private TextView timeError;
    private Button asapBtn;
    private MaterialButton nextBtn;
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
        mapFragment = new MapFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.map_fragment, mapFragment).commit();

        View view = inflater.inflate(R.layout.fragment_passenger_main, container, false);
        pickUpLoc = view.findViewById(R.id.pickUpLocET);
        destination = view.findViewById(R.id.destinationET);
        pickUpTime = view.findViewById(R.id.pickUpTimeET);
        nextBtn = view.findViewById(R.id.nextBtn);
        asapBtn = view.findViewById(R.id.asapButton);
        timeError = view.findViewById(R.id.timeErrorTV);

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
                mTimePicker = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker,  int selectedHour, int selectedMinute) {
                        Utils.changeBtnToGray((MaterialButton) asapBtn, getActivity());

                        pickUpTime.setText( selectedHour + ":" + selectedMinute);
                        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(selectedHour, selectedMinute, 0));
                        if (time.isBefore(LocalDateTime.now())) {
                            time = time.plusDays(1);
                        }
                        if (time.minusHours(5).isAfter(LocalDateTime.now())) {
                            pickUpTime.setError("Schedule max 5h in advance.");
                            timeError.setVisibility(View.VISIBLE);
                            nextBtn.setEnabled(false);
                        } else {
                            nextBtn.setEnabled(true);
                            pickUpTime.setError(null);
                            timeError.setVisibility(View.GONE);
                            RideService.rideInCreation.setScheduledTime(time.toString());
                        }
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        setBroadcastActiveRide();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRideService = new Intent(getContext(), RideService.class);
                intentRideService.putExtra("method", "activeRide");
                requireActivity().startService(intentRideService);
            }
        });

        asapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.changeBtnToBlue((MaterialButton) asapBtn, getActivity());
                nextBtn.setEnabled(true);
                pickUpTime.setText(null);
                pickUpTime.setError(null);
                timeError.setVisibility(View.GONE);
                RideService.rideInCreation.setScheduledTime(null);
            }
        });
        mapFragment.pozovi();
        return view;
    }

    private boolean validateInputs() {
        boolean valid = true;
        if (pickUpLoc.getText().toString().isEmpty()) {
            pickUpLoc.setError("Pickup is required.");
            valid = false;
        }

        if (destination.getText().toString().isEmpty()) {
            destination.setError("Destination is required.");
            valid = false;
        }

        return valid;
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

                        String type = "pickup";
                        if (!isPickup) {
                            type = "destination";
                        }

                        mapFragment.addMarker(place.getLatLng(), type);
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
        if (isPickup)  {
            pickUpLoc.setText(address.toString());
            pickUpLoc.setError(null);
            destination.requestFocus();
        } else {
            destination.setText(address.toString());
            destination.setError(null);
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


    private void setBroadcastActiveRide() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                boolean hasActive = extras.getBoolean("hasActive");
                if (hasActive) {
                    Toast.makeText(getActivity(),"Sorry, you already have active ride.",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!validateInputs())
                        return;

                    if (route != null) {
                        if (route.getDeparture() != null && route.getDestination() != null) {
                            RideService.rideInCreation.getLocations().add(route);
                        }else {
                            Toast.makeText(getActivity(),"Pick locations!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(getActivity(),"Pick locations!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    route = null;
                    FragmentTransition.to(RideSettingsFragment.newInstance(), getActivity(), true, R.id.passengerFL);
                }

            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("activeRide"));

    }


}