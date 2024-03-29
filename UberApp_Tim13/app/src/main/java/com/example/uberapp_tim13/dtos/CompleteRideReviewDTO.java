package com.example.uberapp_tim13.dtos;

import java.io.Serializable;

public class CompleteRideReviewDTO implements Serializable {
    ReviewReturnedDTO vehicleReview;
    ReviewReturnedDTO driverReview;

    public ReviewReturnedDTO getVehicleReview() {
        return vehicleReview;
    }

    public ReviewReturnedDTO getDriverReview() {
        return driverReview;
    }

    @Override
    public String toString() {
        return "CompleteRideReviewDTO{" +
                "vehicleReview=" + vehicleReview +
                ", driverReview=" + driverReview +
                '}';
    }
}
