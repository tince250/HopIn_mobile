package com.example.uberapp_tim13.dtos;

import java.io.Serializable;
import java.util.List;

public class GetRideDetailsDTO implements Serializable {
    private List<LocationDTO> locations;
    private String vehicleType;
    private boolean petTransport;
    private boolean babyTransport;

    public GetRideDetailsDTO() {}

    public GetRideDetailsDTO(RideDTO ride) {
        this.locations = ride.getLocations();
        this.vehicleType = ride.getVehicleType();
        this.petTransport = ride.isPetTransport();
        this.babyTransport = ride.isBabyTransport();
    }

    public GetRideDetailsDTO(List<LocationDTO> locations, String vehicleType, boolean petTransport, boolean babyTransport) {
        this.locations = locations;
        this.vehicleType = vehicleType;
        this.petTransport = petTransport;
        this.babyTransport = babyTransport;
    }

    public List<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDTO> locations) {
        this.locations = locations;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }
}
