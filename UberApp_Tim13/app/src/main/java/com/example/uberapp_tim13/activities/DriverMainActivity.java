package com.example.uberapp_tim13.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.uberapp_tim13.R;

import com.example.uberapp_tim13.dialogs.RideStateDialog;
import com.example.uberapp_tim13.dtos.RideForReportDTO;
import com.example.uberapp_tim13.dtos.RideInInviteDTO;
import com.example.uberapp_tim13.dtos.RideInviteDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;

import com.example.uberapp_tim13.dtos.WorkingHoursDTO;
import com.example.uberapp_tim13.dtos.WorkingHoursEndDTO;
import com.example.uberapp_tim13.dtos.WorkingHoursStartDTO;
import com.example.uberapp_tim13.fragments.AccountFragment;
import com.example.uberapp_tim13.fragments.RideHistoryFragment;
import com.example.uberapp_tim13.fragments.DriverHomeFragment;
import com.example.uberapp_tim13.fragments.InboxFragment;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.StompManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMainActivity extends AppCompatActivity {

    public static WorkingHoursDTO workingHours;
    Switch switchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        setTitle(R.string.home_nav);

        connectToRideOffersSocket();
        connectToScheduledRideSockets();

        FragmentTransition.to(DriverHomeFragment.newInstance(), this, true, R.id.driverFL);


        if (getIntent().getExtras() != null && getIntent().getExtras().get("acceptance") != null) {
            (new RideStateDialog(DriverMainActivity.this, "SCHEDULED", RideService.returnedRide.getScheduledTime())).show();
//            StompManager.subscribeToScheduledRide(getApplicationContext(), RideService.returnedRide);
        }


        setBottomNavigationBar();
    }

    private void connectToScheduledRideSockets() {
        Call<List<RideReturnedDTO>> call = RestUtils.rideAPI.getScheduledRidesForUser(AuthService.tokenDTO.getAccessToken() ,Globals.userId);
        call.enqueue(new Callback<List<RideReturnedDTO>>() {
            @Override
            public void onResponse(Call<List<RideReturnedDTO>> call, Response<List<RideReturnedDTO>> response) {
                if (response.isSuccessful()) {
                    for (RideReturnedDTO ride: response.body()) {
                        StompManager.subscribeToScheduledRide(DriverMainActivity.this, ride);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RideReturnedDTO>> call, Throwable t) {
                Log.e("ERR", t.toString());
            }
        });
    }

    private void connectToRideOffersSocket() {
        StompManager manager = new StompManager();
        manager.connect();
        Log.d("OFFER", "/topic/driver/ride-offers/" + Globals.userId);
        StompManager.stompClient.topic("/topic/driver/ride-offers/" + Globals.userId).subscribe(topicMessage -> {
            RideReturnedDTO ride = Globals.gson.fromJson(topicMessage.getPayload(), RideReturnedDTO.class);
            RideService.returnedRide = ride;

            Log.d("OFFER", "" + ride);
            Intent i = new Intent(DriverMainActivity.this, AcceptanceRideActivity.class);
            i.putExtra("invite", new RideInviteDTO(null, new RideInInviteDTO(ride)));
            i.putExtra("type", "driver-offer");
            startActivity(i);
        });
    }

    public void onClickCurrentRide(View v){
        //TODO: IMPLEMENT
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_upper_menu, menu);
        setBroadcast();
        MenuItem switchItem = menu.findItem(R.id.activeToggle);
        switchItem.setActionView(R.layout.toggle_button_bar);
        switchBtn = (Switch) menu.findItem(R.id.activeToggle).getActionView().findViewById(R.id.activeSwitch);
//        if (!Globals.isActive) {
//            switchBtn.setChecked(false);
//        }
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    WorkingHoursStartDTO start = new WorkingHoursStartDTO(LocalDateTime.now().withNano(0).toString());
                    Call<WorkingHoursDTO> call = RestUtils.driverAPI.addWorkingHours(AuthService.tokenDTO.getAccessToken(), Globals.user.getId(), start);
                    call.enqueue(new Callback<WorkingHoursDTO>() {
                        @Override
                        public void onResponse(Call<WorkingHoursDTO> call, Response<WorkingHoursDTO> response) {
                            if (response.isSuccessful()) {
                                workingHours = response.body();
                                Globals.isActive = true;
                                Log.d("hours", workingHours.toString());
                            } else {
                                switchBtn.setChecked(false);
                                Globals.isActive = true;
                                Toast.makeText(DriverMainActivity.this, "You exceeded the 8 hours limit!", Toast.LENGTH_LONG);
                            }
                        }

                        @Override
                        public void onFailure(Call<WorkingHoursDTO> call, Throwable t) {
                            Log.d("EVOME", t.toString());
                            Toast.makeText(DriverMainActivity.this, "An error happened while trying to fetch rides :(", Toast.LENGTH_LONG);
                        }
                    });
                    Toast.makeText(getBaseContext(), "You are ready!", Toast.LENGTH_SHORT).show();
                } else {
                    WorkingHoursEndDTO end = new WorkingHoursEndDTO(LocalDateTime.now().withNano(0).toString());
                    Call<WorkingHoursDTO> call = RestUtils.driverAPI.updateWorkingHours(AuthService.tokenDTO.getAccessToken(), workingHours.getId(), end);
                    call.enqueue(new Callback<WorkingHoursDTO>() {
                        @Override
                        public void onResponse(Call<WorkingHoursDTO> call, Response<WorkingHoursDTO> response) {
                            if (response.isSuccessful()) {
                                workingHours = response.body();
                                Globals.isActive = false;
                                Log.d("hours", workingHours.toString());
                            } else {
                                Toast.makeText(DriverMainActivity.this, "An error happened while trying to fetch rides.", Toast.LENGTH_LONG);
                            }
                        }

                        @Override
                        public void onFailure(Call<WorkingHoursDTO> call, Throwable t) {
                            Log.d("EVOME", t.toString());
                            Toast.makeText(DriverMainActivity.this, "An error happened while trying to fetch rides :(", Toast.LENGTH_LONG);
                        }
                    });
                    Toast.makeText(getBaseContext(), "You're inactive, thanks for your effort.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return true;
    }

    public void setBroadcast() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean active = (boolean) intent.getExtras().get("active");
                if (!active) {
                    switchBtn.setChecked(true);
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("isActive"));

    }


    public void setBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            //redirection to other activities/fragments
            switch (item.getItemId()) {
                case R.id.nav_inbox:
                    setTitle("Inbox");
                    FragmentTransition.to(InboxFragment.newInstance(), this, true, R.id.driverFL);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.nav_home:
                    setTitle("Home");
                    FragmentTransition.to(DriverHomeFragment.newInstance(), this, true, R.id.driverFL);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.nav_profile:
                    setTitle("Account");
                    FragmentTransition.to(AccountFragment.newInstance(), this, true, R.id.driverFL);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.nav_history:
                    setTitle("History");
                    FragmentTransition.to(RideHistoryFragment.newInstance(), this, true, R.id.driverFL);
                    overridePendingTransition(0, 0);
                    break;
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
}