package com.example.uberapp_tim13.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dialogs.DriverDetailsDialog;
import com.example.uberapp_tim13.dialogs.PanicReasonDialog;
import com.example.uberapp_tim13.dialogs.PassengerDetailsDialog;
import com.example.uberapp_tim13.dtos.PanicRideDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserInRideDTO;
import com.example.uberapp_tim13.fragments.MapFragment;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.model.User;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.tools.Globals;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentRideActivity extends AppCompatActivity {

    public static RideReturnedDTO ride;
    private Activity context;

    private Chronometer timer;
    private ImageView chatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ride = (RideReturnedDTO) getIntent().getExtras().get("ride");

        getSupportFragmentManager().beginTransaction().replace(R.id.map_container_current, new MapFragment(ride)).commit();

        context=this;


        setTitle("Current ride");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_current_ride);

        chatBtn = findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CurrentRideActivity.this, ChatActivity.class);
                if(Globals.userRole.equals("driver")) {
                    i.putExtra("receiverId", ride.getPassengers().get(0).getId());
                } else {
                    i.putExtra("receiverId", ride.getDriver().getId());
                }

                i.putExtra("rideId", ride.getId());

                startActivity(i);
            }
        });

        MaterialButton panicBtn = this.findViewById(R.id.panicBtn);
        panicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PanicReasonDialog(context, ride).show();
            }
        });

        timer = findViewById(R.id.timePassedTV);

        fitActivityToRole();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (Globals.userRole.equals("driver")) {
                    startActivity(new Intent(this, DriverMainActivity.class));
                    return true;
                } else {
                    startActivity(new Intent(this, PassengerMainActivity.class));
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void fitActivityToRole() {
        LinearLayout startFinishBtns = findViewById(R.id.start_finish_buttons);
        Button inconsistentBtn = findViewById(R.id.inconsistentBtn);
        View driverDetails = findViewById(R.id.driverDetailsCardCV);
        View passDetails = findViewById(R.id.passDetailsCardLL);
        switch (Globals.userRole) {
            case "driver":
                driverDetails.setVisibility(View.GONE);
                inconsistentBtn.setVisibility(View.GONE);

                startFinishBtns.setVisibility(View.VISIBLE);
                addListenersToStartFinishBtns();

                passDetails.setVisibility(View.VISIBLE);
                passDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new PassengerDetailsDialog(context, ride).show();
                    }
                });
                break;
            case "passenger":
                passDetails.setVisibility(View.GONE);
                startFinishBtns.setVisibility(View.GONE);

                if (Globals.userId != ride.getPassengers().get(0).getId()) {
                    chatBtn.setEnabled(false);
                }

                driverDetails.setVisibility(View.VISIBLE);
                driverDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DriverDetailsDialog(context, ride).show();
                    }
                });
                inconsistentBtn.setVisibility(View.VISIBLE);
                break;
        }

//        findViewById(R.id.panicBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new PanicReasonDialog(context, ride).show();
//            }
//        });
    }

    private void addListenersToStartFinishBtns() {
        Button start = findViewById(R.id.startBtn);
        Button finish = findViewById(R.id.finishBtn);

        start.setEnabled(true);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setEnabled(false);
                timer.setBase(SystemClock.elapsedRealtime());
                finish.setEnabled(true);

                Call<RideReturnedDTO> call = RestUtils.rideAPI.startRide(ride.getId());
                call.enqueue(new Callback<RideReturnedDTO>() {
                    @Override
                    public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response) {
                        timer.start();
                    }

                    @Override
                    public void onFailure(Call<RideReturnedDTO> call, Throwable t) {
                        Log.e("error", "Error while trying to start ride.");
                    }
                });
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.stop();

                Call<RideReturnedDTO> call = RestUtils.rideAPI.finishRide(ride.getId());
                call.enqueue(new Callback<RideReturnedDTO>() {
                    @Override
                    public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response) {

                    }

                    @Override
                    public void onFailure(Call<RideReturnedDTO> call, Throwable t) {
                        Log.e("error", "Error while trying to finish ride.");
                    }
                });
            }
        });
        finish.setEnabled(false);
    }
}
