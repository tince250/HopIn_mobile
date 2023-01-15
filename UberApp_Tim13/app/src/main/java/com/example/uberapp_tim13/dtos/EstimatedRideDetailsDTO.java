package com.example.uberapp_tim13.dtos;

import java.io.Serializable;

public class EstimatedRideDetailsDTO implements Serializable  {
    int estimatedTimeInMinutes;
    int estimatedCost;
    double estimatedDistance;

    public EstimatedRideDetailsDTO() {}

    public EstimatedRideDetailsDTO(int estimatedTimeInMinutes, int estimatedCost, double estimatedDistance) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.estimatedCost = estimatedCost;
        this.estimatedDistance = estimatedDistance;
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public int getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(int estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public double getEstimatedDistance() {
        return estimatedDistance;
    }

    public void setEstimatedDistance(double estimatedDistance) {
        this.estimatedDistance = estimatedDistance;
    }
}
