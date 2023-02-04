package com.example.uberapp_tim13.activities;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dialogs.DeclineReasonDialog;
import com.example.uberapp_tim13.dialogs.DriverDetailsDialog;
import com.example.uberapp_tim13.dialogs.PanicReasonDialog;
import com.example.uberapp_tim13.dialogs.PassengerDetailsDialog;
import com.example.uberapp_tim13.dialogs.RateRideDialog;
import com.example.uberapp_tim13.dtos.InboxReturnedDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.dtos.TimerDTO;
import com.example.uberapp_tim13.fragments.MapFragment;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.AuthService;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.Globals;
import com.google.android.material.button.MaterialButton;
import com.example.uberapp_tim13.tools.StompManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentRideActivity extends AppCompatActivity {

    public static RideReturnedDTO ride;
    private Activity context;

    Handler handler = new Handler(Looper.getMainLooper());

    private Chronometer timer;
    private ImageView chatBtn;
    private ImageView callBtn;

    private MaterialButton startBtn;
    private MaterialButton finishBtn;
    private MaterialButton cancelBtn;
    private Chronometer timePassedTV;
    private TextView arrivalTimeTitleTV;
    private TextView arrivalTimeTV;
    private StompManager vehicleArrivalTimeSM;
    private StompManager vehicleArrivedSM;

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

        startBtn = findViewById(R.id.startBtn);
        finishBtn = findViewById(R.id.finishBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        chatBtn = findViewById(R.id.chatBtn);

        timePassedTV = findViewById(R.id.timePassedTV);
        arrivalTimeTitleTV = findViewById(R.id.arrivalTimeTitleTV);
        arrivalTimeTV = findViewById(R.id.arrivalTimeTV);

        vehicleArrivalTimeSM = new StompManager();
        vehicleArrivedSM = new StompManager();

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int otherId = Globals.user.getId() == ride.getDriver().getId()? ride.getPassengers().get(0).getId(): ride.getDriver().getId();
                Call<InboxReturnedDTO> call = RestUtils.userApi.getRideInbox(AuthService.tokenDTO.getAccessToken(), otherId, ride.getId());
                call.enqueue(new Callback<InboxReturnedDTO>() {
                    @Override
                    public void onResponse(Call<InboxReturnedDTO> call, Response<InboxReturnedDTO> response) {
                        if (response.isSuccessful()) {
                            Intent i = new Intent(CurrentRideActivity.this, ChatActivity.class);
                            i.putExtra("inbox", response.body());
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<InboxReturnedDTO> call, Throwable t) {
                        Log.d("ERROR", "Error fetching inbox");
                    }
                });
            }
        });

        callBtn = findViewById(R.id.callBtn);
        callBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phoneNumber = "";
                if (Globals.userRole.equals("driver")){
                    phoneNumber = ride.getPassengers().get(0).getTelephoneNumber();
                } else {
                    phoneNumber = ride.getDriver().getTelephoneNumber();
                }
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
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
                subscribeToVehicleArrivalTime();
                subscribeToVehicleArrived();
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
                subscribeToVehicleArrivalTime();
                subscribeToVehicleArrived();
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
            RideReturnedDTO rideDTO = Globals.gson.fromJson(topicMessage.getPayload(), RideReturnedDTO.class);
            if (rideDTO.getStatus().equals("STARTED")) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("ORDER_MESSAGE", "START");
                        arrivalTimeTitleTV.setVisibility(View.GONE);
                        arrivalTimeTV.setVisibility(View.GONE);
                        timePassedTV.setVisibility(View.VISIBLE);

                        //TODO: unsubscribe from arrival time sockets using unsubscribe method for stomp manager when driver started the ride. You should set visibility to gone or visible

                        timer.setBase(SystemClock.elapsedRealtime());
                        timer.start();


                    }
                });
            } else {
                if (rideDTO.getStatus().equals("FINISHED")) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            Log.d("ORDER_MESSAGE", "FINISH");
                            addFinishedRideNotification();
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

    private void subscribeToVehicleArrived(){
        vehicleArrivedSM.stompClient.topic("/topic/vehicle-arrival/" + ride.getId()).subscribe(topicMessage -> {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    arrivalTimeTV.setText("Arrived!");
                    vehicleArrivalTimeSM.disconnect();
                    vehicleArrivedSM.disconnect();
                    addVehicleArrivedNotification();
                }
            });
        });
    }

    private void subscribeToVehicleArrivalTime(){
        vehicleArrivalTimeSM.stompClient.topic("/topic/arrival-time/" + ride.getId()).subscribe(topicMessage -> {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    TimerDTO timerDTO = Globals.gson.fromJson(topicMessage.getPayload(), TimerDTO.class);
                    arrivalTimeTV.setText(formatTime(timerDTO.timer));
                }
            });
        });
    }

    private String formatTime(double timer){
        double timerr = Math.floor(timer);
        double minutes = Math.floor(timerr/60);
        double seconds = timerr - minutes*60;
        if (minutes > 0) {
            return minutes + "min" + " " + seconds + "s";
        } else {
            return seconds + "s";
        }
    }
    private void addListenersToStartFinishBtns() {
        startBtn.setEnabled(true);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBtn.setEnabled(false);
                finishBtn.setEnabled(true);

                Log.d("ORDER_MESSAGE", "START");
                arrivalTimeTitleTV.setVisibility(View.GONE);
                arrivalTimeTV.setVisibility(View.GONE);
                timePassedTV.setVisibility(View.VISIBLE);
                timer.setBase(SystemClock.elapsedRealtime());

                Call<RideReturnedDTO> call = RestUtils.rideAPI.startRide(AuthService.tokenDTO.getAccessToken(), ride.getId());
                call.enqueue(new Callback<RideReturnedDTO>() {
                    @Override
                    public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response) {
                        timer.start();
                        finishBtn.setVisibility(View.VISIBLE);
                        cancelBtn.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<RideReturnedDTO> call, Throwable t) {
                        Log.e("error", "Error while trying to start ride.");
                    }
                });
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.stop();

                Call<RideReturnedDTO> call = RestUtils.rideAPI.finishRide(AuthService.tokenDTO.getAccessToken(), ride.getId());
                call.enqueue(new Callback<RideReturnedDTO>() {
                    @Override
                    public void onResponse(Call<RideReturnedDTO> call, Response<RideReturnedDTO> response) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                new MaterialAlertDialogBuilder(CurrentRideActivity.this, R.style.AlertDialogTheme)
                                        .setTitle(R.string.finishedAlert)
                                        .setMessage(R.string.finishedAlertContent)
                                        .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                CurrentRideActivity.this.finish();
                                            }
                                        })
                                        .show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<RideReturnedDTO> call, Throwable t) {
                        Log.e("error", "Error while trying to finish ride.");
                    }
                });
            }
        });
        finishBtn.setEnabled(false);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.stop();
                new DeclineReasonDialog(CurrentRideActivity.this, RideService.returnedRide.getId()).show();
            }
        });
    }


    private void addVehicleArrivedNotification() {
        NotificationManager notificationManager = this.createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Driver has arrived.")
                .setContentText("Vehicle is waiting at the departure.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }

    private void addFinishedRideNotification() {
        NotificationManager notificationManager = this.createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Ride is finished!")
                .setContentText("You can now rate the ride and order new rides.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }


    private NotificationManager createNotificationChannel() {
        CharSequence name = "my_channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("0", name, importance);
        NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        return notificationManager;
    }
}
