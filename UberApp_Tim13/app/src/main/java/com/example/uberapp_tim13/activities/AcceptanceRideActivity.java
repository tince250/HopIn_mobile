package com.example.uberapp_tim13.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.uberapp_tim13.R;
import com.example.uberapp_tim13.dialogs.DeclineReasonDialog;
import com.example.uberapp_tim13.dtos.InvitationResponseDTO;
import com.example.uberapp_tim13.dtos.LocationNoIdDTO;
import com.example.uberapp_tim13.dtos.RideInInviteDTO;
import com.example.uberapp_tim13.dtos.RideInviteDTO;
import com.example.uberapp_tim13.dtos.RideReturnedDTO;
import com.example.uberapp_tim13.fragments.MapFragment;
import com.example.uberapp_tim13.model.Ride;
import com.example.uberapp_tim13.rest.RestUtils;
import com.example.uberapp_tim13.services.RideService;
import com.example.uberapp_tim13.tools.Globals;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class AcceptanceRideActivity extends AppCompatActivity {

    Dialog declineReasonDialog;
    RideInviteDTO invite;

    private StompClient stompClient;

    // because activity is used for both ride offers for drivers and ride invitation to other passsengers
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        invite = (RideInviteDTO) getIntent().getExtras().get("invite");
        type = (String) getIntent().getExtras().get("type");

        if (type == "driver-offer")
            setTitle("Ride offer");
        else
            setTitle("Ride invitation from " + invite.getFrom().getName());

        setContentView(R.layout.activity_acceptance_ride);

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:4321/api/socket/websocket");

        getSupportFragmentManager().beginTransaction().replace(R.id.mapAcceptanceRouteCV, new MapFragment(new RideReturnedDTO(invite.getRide()))).commit();
        fillRideInfo();

        declineReasonDialog = new DeclineReasonDialog(this, RideService.returnedRide.getId());
    }

    private void fillRideInfo() {
        RideInInviteDTO ride = invite.getRide();
        LocationNoIdDTO pickup = ride.getPickup();
        LocationNoIdDTO destination = ride.getDestination();
        ((TextView)findViewById(R.id.pickUpLocationTV)).append(pickup.getAddress());
        ((TextView)findViewById(R.id.destinationTV)).append(destination.getAddress());
        ((TextView)findViewById(R.id.distanceTV)).append(ride.getDistance() + "");
        ((TextView)findViewById(R.id.durationTV)).append(ride.getPassengers().size() + "");
        ((TextView)findViewById(R.id.priceTV)).append(ride.getPrice() + "");
    }

    public void onClickDecline(View v){
        if (type == "driver-offer")
            declineReasonDialog.show();
        else {
            stompClient.send("/ws/send/invite-response/" + invite.getFrom().getId() , Globals.gson.toJson(new InvitationResponseDTO(Globals.userId, false)));
        }
    }

    public void onClickAccept(View v){
        if (type == "driver-offer")
            RestUtils.rideAPI.acceptRide(RideService.returnedRide.getId());
        else {
            stompClient.send("/ws/send/invite-response/" + invite.getFrom().getId() , Globals.gson.toJson(new InvitationResponseDTO(Globals.userId, true)));
            // podesiti da sad i on slusa order ride odgovor od drivera
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stompClient.disconnect();
    }
}