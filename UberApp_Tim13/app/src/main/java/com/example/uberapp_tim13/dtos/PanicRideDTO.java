package com.example.uberapp_tim13.dtos;

import java.time.LocalDateTime;

public class PanicRideDTO {
    private int id;
    private UserInPanicRideDTO user;
    private RideReturnedDTO ride;
    private LocalDateTime time;
    private String reason;


    public int getId() {
        return id;
    }

    public UserInPanicRideDTO getUser() {
        return user;
    }

    public RideReturnedDTO getRide() {
        return ride;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getReason() {
        return reason;
    }
}
