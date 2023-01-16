package com.example.uberapp_tim13.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dtos.RideInviteDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.fragments.AccountFragment;
import com.example.uberapp_tim13.fragments.InboxFragment;
import com.example.uberapp_tim13.fragments.PassengerHomeFragment;
import com.example.uberapp_tim13.fragments.RideHistoryFragment;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.tools.FragmentTransition;
import com.example.uberapp_tim13.tools.Globals;
import com.example.uberapp_tim13.tools.StompManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class PassengerMainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);
        setTitle("Home");

        connectToRideInvitesSocket();

        FragmentTransition.to(PassengerHomeFragment.newInstance(), this, true, R.id.passengerFL);

        setBottomNavigationBar();
    }

    private void connectToRideInvitesSocket() {
        StompManager manager = new StompManager();
        manager.connect();
        StompManager.stompClient.topic("/topic/invites/" + Globals.userId).subscribe(topicMessage -> {
            RideInviteDTO invite = Globals.gson.fromJson(topicMessage.getPayload(), RideInviteDTO.class);
            Log.d("prov", "" + invite.getRide());
            Intent i = new Intent(PassengerMainActivity.this, AcceptanceRideActivity.class);
            i.putExtra("invite", invite);
            i.putExtra("type", "invite");
            startActivity(i);
        });
    }

    public void onClickNext(View v){
        startActivity(new Intent(PassengerMainActivity.this, FavoriteRoutesActivity.class));
    }


    // Sets event listeners for the bottom nav bar
    public void setBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            //redirection to other activities/fragments
            switch (item.getItemId()) {
                case R.id.nav_inbox:
                    setTitle("Inbox");
                    FragmentTransition.to(InboxFragment.newInstance(), this, true, R.id.passengerFL);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.nav_home:
                    setTitle("Home");
                    FragmentTransition.to(PassengerHomeFragment.newInstance(), this, true, R.id.passengerFL);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.nav_profile:
                    setTitle("Account");
                    FragmentTransition.to(AccountFragment.newInstance(), this, true, R.id.passengerFL);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.nav_history:
                    setTitle("History");
                    FragmentTransition.to(RideHistoryFragment.newInstance(), this, true, R.id.passengerFL);
                    overridePendingTransition(0, 0);
                    break;
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.passenger_upper_menu, menu);

        MenuItem switchItem = menu.findItem(R.id.liveRide);
        switchItem.setActionView(R.layout.current_ride_button_bar);
        final Button liveBtn = (Button) menu.findItem(R.id.liveRide).getActionView().findViewById(R.id.currentRideBarBtn);
        liveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "go to current ride", Toast.LENGTH_LONG);
//                startActivity(new Intent(PassengerMainActivity.this, CurrentRideActivity.class));
                Intent i = new Intent(PassengerMainActivity.this, ChatActivity.class);
                i.putExtra("receiverId", 2);
                i.putExtra("rideId", 1);

                startActivity(i);
            }
        });
        return true;
    }

    public void addStops(View v) {
        Toast.makeText(this, "add stops dialog should pop up", Toast.LENGTH_LONG);
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

        StompManager.stompClient.disconnect();
    }
}