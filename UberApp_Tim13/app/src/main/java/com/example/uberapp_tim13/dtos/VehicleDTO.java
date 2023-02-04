package com.example.uberapp_tim13.dtos;

import java.io.Serializable;

public class VehicleDTO implements Serializable{
    private int id;
    private String vehicleType;
    private String model;
    private String licenseNumber;
    private LocationNoIdDTO currentLocation;
    private int passengerSeats;
    private boolean babyTransport;
    private boolean petTransport;

    public int getId() {
        return id;
    }
    public String getVehicleType() {
        return vehicleType;
    }

    public String getModel() {
        return model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public LocationNoIdDTO getCurrentLocation() {
        return currentLocation;
    }
}
