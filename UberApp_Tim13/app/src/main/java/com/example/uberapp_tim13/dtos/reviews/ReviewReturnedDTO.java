package com.example.uberapp_tim13.dtos.reviews;

import com.example.uberapp_tim13.dtos.rides.UserInRideDTO;

import java.io.Serializable;

public class ReviewReturnedDTO implements Serializable {
    private int id;
    private int rating;
    private String comment;
    private UserInRideDTO passenger;

    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public UserInRideDTO getPassenger() {
        return passenger;
    }

    @Override
    public String toString() {
        return "ReviewReturnedDTO{" +
                "id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", passenger=" + passenger +
                '}';
    }
}
