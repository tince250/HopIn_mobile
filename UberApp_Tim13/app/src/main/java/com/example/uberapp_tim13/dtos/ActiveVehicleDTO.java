package com.example.uberapp_tim13.dtos;

public class ActiveVehicleDTO {
    private int vehicleId;
    private int driverId;
    private LocationNoIdDTO currentLocation;
    private String status;

    public ActiveVehicleDTO(int vehicleId, int driverId, LocationNoIdDTO currentLocation, String status) {
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.currentLocation = currentLocation;
        this.status = status;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public LocationNoIdDTO getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LocationNoIdDTO currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
