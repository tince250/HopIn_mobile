package com.example.uberapp_tim13.dtos;

import java.io.Serializable;
import java.util.List;

public class GetRideDetailsDTO implements Serializable {
    private List<LocationDTO> locations;
    private String vehicleType;

    public GetRideDetailsDTO() {}

    public GetRideDetailsDTO(RideDTO ride) {
        this.locations = ride.getLocations();
        this.vehicleType = ride.getVehicleType();
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
}
