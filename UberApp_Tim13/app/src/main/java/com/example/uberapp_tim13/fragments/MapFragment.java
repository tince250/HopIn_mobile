package com.example.uberapp_tim13.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.uberapp_tim13.BuildConfig;
import com.example.uberapp_tim13.activities.AcceptanceRideActivity;
import com.example.uberapp_tim13.activities.PassengerMainActivity;
import com.example.uberapp_tim13.dialogs.LocationDialog;
import com.example.uberapp_tim13.dtos.ActiveVehicleDTO;
import com.example.uberapp_tim13.dtos.LocationDTO;
import com.example.uberapp_tim13.dtos.LocationNoIdDTO;
import com.example.uberapp_tim13.dtos.RideInviteDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserReturnedDTO;
import com.example.uberapp_tim13.dtos.VehicleDTO;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.services.DriverService;
import com.example.uberapp_tim13.services.UserService;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.StompManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.uberapp_tim13.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationManager locationManager;
    StompManager manager;
    private String provider;
    private SupportMapFragment mMapFragment;
    private AlertDialog dialog;
    private GoogleMap map;

    private Marker pickup;
    private Marker destination;
    private Marker here;

    private Polyline polyline;
    RideReturnedDTO ride = null;

    private String current_type = "pickup";

    private Map<Integer, Marker> vehicleMarkers = new HashMap<Integer, Marker>();
    private Map<Integer, ActiveVehicleDTO> vehicles = new HashMap<Integer, ActiveVehicleDTO>();


    public MapFragment() {
        // Required empty public constructor
    }


    public MapFragment(RideReturnedDTO ride) {
        this.ride = ride;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        createMapFragmentAndInflate();
        manager = new StompManager();
        manager.connect();
        this.setMarkersForActiveVehiclesOnCreate();
        this.connectToVehiclesOnMapSockets();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Checking if providers are enabled - dynamic permissions
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
        if (!gps && !wifi) {
            this.showLocationDialog();
        } else {
            requestNeededPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNeededPermissions() {
        if (checkLocationPermissions()) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

                //Request location updates:
                locationManager.requestLocationUpdates(provider, 2000, 0, this);
            }else if(ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                //Request location updates:
                locationManager.requestLocationUpdates(provider, 2000, 0, this);
            }
        }
    }

    private boolean checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                        .setTitle("Allow user location")
                        .setMessage("To continue working we need your locations....Allow now?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private void showLocationDialog() {
        if (dialog == null) {
            dialog = new LocationDialog(getActivity()).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        dialog.show();
        setDialogButtonsAndColors();
    }

    public void setDialogButtonsAndColors() {
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(getContext().getResources().getColor(R.color.dark_blue));
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getContext().getResources().getColor(R.color.dark_blue));
    }

    private void createMapFragmentAndInflate() {
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        mMapFragment = SupportMapFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mMapFragment).commit();


        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (map != null) {
//            addMarker(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        Location location = null;

        if (provider == null) {
            showLocationDialog();
        } else {
            if (checkLocationPermissions()) {
                Log.i("ASD", "str" + provider);


                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    location = locationManager.getLastKnownLocation(provider);
                } else if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    location = locationManager.getLastKnownLocation(provider);
                }
            }
        }

        // setting map to current user's location


//        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(@NonNull LatLng latLng) {
//                Location temp = new Location(LocationManager.GPS_PROVIDER);
//                temp.setLatitude(latLng.latitude);
//                temp.setLongitude(latLng.longitude);
//                addMarker(temp, current_type);
//            }
//        });
//
//        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Toast.makeText(getActivity(), marker.getTitle(), Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        Location finalLocation = location;
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (ride == null && finalLocation != null) {
                    addMarker(new LatLng(finalLocation.getLatitude(), finalLocation.getLongitude()), "here");
                } else {
                    if (ride != null)
                        displayRoute();
                }
            }
        });

    }

    private void displayRoute() {
        LocationDTO loc = ride.getLocations().get(0);
        LocationNoIdDTO pickupLoc = loc.getDeparture();
        LatLng pickup = new LatLng(pickupLoc.getLatitude(), pickupLoc.getLongitude());

        LocationNoIdDTO destLoc = loc.getDestination();
        LatLng destination = new LatLng(destLoc.getLatitude(),destLoc.getLongitude());

        addMarker(pickup, "pickup");
        addMarker(destination, "destination");

        List<LatLng> path = new ArrayList();

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(BuildConfig.MAPS_API_KEY)
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, pickup.latitude + "," + pickup.longitude,
                destination.latitude + "," + destination.longitude);

        try {
            DirectionsResult res = req.await();
            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e("error", ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.parseColor("#337D98")).width(12);
            polyline = map.addPolyline(opts);
        }

        //set camera to show whole route
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(destination);
        builder.include(pickup);
        LatLngBounds bounds = builder.build();



        int padding = 120; // padding around start and end marker
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);

        map.getUiSettings().setZoomControlsEnabled(true);
    }

    public Marker addMarker(LatLng loc, String type) {

        Marker marker = null;
        String title = "";
        switch (type) {
            case "here":
                marker = here;
                title = "You're here!";
                break;
            case "pickup":
                marker = pickup;
                title = "A";
                break;
            case "destination":
                marker = destination;
                title = "B";
                break;
        }

        if (marker != null) {
            marker.remove();
        }

        BitmapDescriptor markerIcon = null;
        if (!type.equals("vehicle"))
            markerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        else
            markerIcon = (BitmapDescriptorFactory.fromResource(R.drawable.vehicle));

        marker = map.addMarker(new MarkerOptions()
                .title(title)
                .icon(markerIcon)
                .position(loc));
        marker.setFlat(true);

        if (!type.equals("vehicle")) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(loc).zoom(10).build();

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        return marker;
    }

    public void setMarkersForActiveVehiclesOnCreate(){
        Call<List<ActiveVehicleDTO>> call = RestUtils.driverAPI.getActiveVehicles();
        call.enqueue(new Callback<List<ActiveVehicleDTO>>() {

            @Override
            public void onResponse(Call<List<ActiveVehicleDTO>> call, Response<List<ActiveVehicleDTO>> response){
                List<ActiveVehicleDTO> activeVehicles = response.body();
                for (ActiveVehicleDTO vehicle : activeVehicles){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            vehicles.put(vehicle.getVehicleId(), vehicle);
                            Marker marker = addMarker(new LatLng(vehicle.getCurrentLocation().getLatitude(), vehicle.getCurrentLocation().getLongitude()), "vehicle");
                            vehicleMarkers.put(vehicle.getVehicleId(), marker);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ActiveVehicleDTO>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    public void connectToVehiclesOnMapSockets(){
        this.connectToVehicleActivation();
        this.connectToVehicleDeactivation();
    }

    private void connectToVehicleActivation() {
        StompManager.stompClient.topic("/topic/vehicle/activation").subscribe(topicMessage -> {
            int driverId = Globals.gson.fromJson(topicMessage.getPayload(), Integer.class);
            this.setMarkerForVehicleFromSocketIncome(driverId, true);
        });
    }

    private void connectToVehicleDeactivation(){
        StompManager manager = new StompManager();
        manager.connect();
        StompManager.stompClient.topic("/topic/vehicle/deactivation").subscribe(topicMessage -> {
            int driverId = Globals.gson.fromJson(topicMessage.getPayload(), Integer.class);
            this.setMarkerForVehicleFromSocketIncome(driverId, false);
        });
    }

    private void setMarkerForVehicleFromSocketIncome(int driverId, boolean activation){
        Call<VehicleDTO> call = RestUtils.driverAPI.getVehicle(AuthService.tokenDTO.getAccessToken(),
                driverId);
        call.enqueue(new Callback<VehicleDTO>() {

            @Override
            public void onResponse(Call<VehicleDTO> call, Response<VehicleDTO> response){
                ActiveVehicleDTO vehicle = new ActiveVehicleDTO(response.body(), driverId);
                if (activation) {
                    if (vehicles.get(vehicle.getVehicleId()) == null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                vehicles.put(vehicle.getVehicleId(), vehicle);
                                Marker marker = addMarker(new LatLng(vehicle.getCurrentLocation().getLatitude(), vehicle.getCurrentLocation().getLongitude()), "vehicle");
                                vehicleMarkers.put(vehicle.getVehicleId(), marker);
                            }
                        });
                    }
                } else {
                    vehicles.remove(vehicle.getVehicleId());
                    vehicleMarkers.get(vehicle.getVehicleId()).remove();
                    vehicleMarkers.remove(vehicle.getVehicleId());
                }
            }

            @Override
            public void onFailure(Call<VehicleDTO> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }




    @Override
    public void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
    }
}