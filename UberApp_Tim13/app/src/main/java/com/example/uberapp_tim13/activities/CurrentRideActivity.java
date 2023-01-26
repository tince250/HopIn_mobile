package com.example.uberapp_tim13.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.uberapp_tim13.dialogs.RateRideDialog;
import com.example.uberapp_tim13.dtos.PanicRideDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.UserInRideDTO;
import com.example.uberapp_tim13.fragments.MapFragment;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.model.User;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.Globals;
import com.google.android.material.button.MaterialButton;
import com.example.uberapp_tim13.tools.StompManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentRideActivity extends AppCompatActivity {

    public static RideReturnedDTO ride;
    private Activity context;

    Handler handler = new Handler(Looper.getMainLooper());

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
                subscribeToStartFinishMessages();
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

    private void subscribeToStartFinishMessages() {
        StompManager.stompClient.topic("/topic/ride-start-finish/" + ride.getDriver().getId()).subscribe(topicMessage -> {
            if (topicMessage.getPayload().trim().equals("start")) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("ORDER_MESSAGE", "START");
                        timer.setBase(SystemClock.elapsedRealtime());
                        timer.start();
                    }
                });
            } else {
                if (topicMessage.getPayload().trim().equals("finish")) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            Log.d("ORDER_MESSAGE", "FINISH");
                            timer.stop();
                            try {
                                Thread.sleep(1000);
                                new RateRideDialog(CurrentRideActivity.this, ride, "currentRide", null).show();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
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

                Call<RideReturnedDTO> call = RestUtils.rideAPI.startRide(AuthService.tokenDTO.getAccessToken(), ride.getId());
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

                Call<RideReturnedDTO> call = RestUtils.rideAPI.finishRide(AuthService.tokenDTO.getAccessToken(), ride.getId());
                call.enqueue(new Callback<RideReturnedDTO>() {
                    @Override
                    public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response) {
                        new MaterialAlertDialogBuilder(CurrentRideActivity.this, R.style.AlertDialogTheme)
                                .setTitle(R.string.finishedAlert)
                                .setMessage(R.string.finishedAlertContent)
                                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getParent().finish();
                                    }
                                })
                                .show();
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
