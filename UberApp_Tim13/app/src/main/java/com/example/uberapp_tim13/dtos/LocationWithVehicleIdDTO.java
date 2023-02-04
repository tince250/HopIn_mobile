package com.example.uberapp_tim13.dtos;

public class LocationWithVehicleIdDTO {

    private int vehicleId;
    private String address;
    private Double latitude;
    private Double longitude;

    public LocationWithVehicleIdDTO(int id, LocationNoIdDTO vehicleLocation) {
        super();
        this.vehicleId = id;
        this.address = vehicleLocation.getAddress();
        this.latitude = vehicleLocation.getLatitude();
        this.longitude = vehicleLocation.getLongitude();
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}