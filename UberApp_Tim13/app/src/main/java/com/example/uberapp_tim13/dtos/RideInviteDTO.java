package com.example.uberapp_tim13.dtos;

import com.example.uberapp_tim13.model.Ride;

import java.io.Serializable;

public class RideInviteDTO implements Serializable {
    private UserDTO from;
    private RideInInviteDTO ride;

    public RideInviteDTO(UserDTO from, RideInInviteDTO ride) {
        this.from = from;
        this.ride = ride;
    }

    public UserDTO getFrom() {
        return from;
    }

    public void setFrom(UserDTO from) {
        this.from = from;
    }

    public RideInInviteDTO getRide() {
        return ride;
    }

    public void setRide(RideInInviteDTO ride) {
        this.ride = ride;
    }
}
