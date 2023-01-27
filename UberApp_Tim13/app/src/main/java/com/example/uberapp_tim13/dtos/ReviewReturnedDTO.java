package com.example.uberapp_tim13.dtos;

import com.example.uberapp_tim13.dtos.UserInRideDTO;

import java.io.Serializable;

public class ReviewReturnedDTO implements Serializable {
    private int id;
    private int rating;
    private String comment;
    private UserReturnedDTO passenger;

    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public UserReturnedDTO getPassenger() {
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
