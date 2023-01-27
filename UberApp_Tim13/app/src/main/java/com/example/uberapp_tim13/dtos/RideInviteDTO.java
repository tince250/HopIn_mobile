package com.example.uberapp_tim13.dtos;

import java.io.Serializable;

public class RideInviteDTO implements Serializable {
    private UserReturnedDTO from;
    private RideInInviteDTO ride;

    public RideInviteDTO(UserReturnedDTO from, RideInInviteDTO ride) {
        this.from = from;
        this.ride = ride;
    }

    public UserReturnedDTO getFrom() {
        return from;
    }

    public void setFrom(UserReturnedDTO from) {
        this.from = from;
    }

    public RideInInviteDTO getRide() {
        return ride;
    }

    public void setRide(RideInInviteDTO ride) {
        this.ride = ride;
    }
}
