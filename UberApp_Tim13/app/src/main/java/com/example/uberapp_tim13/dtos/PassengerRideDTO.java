package com.example.uberapp_tim13.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassengerRideDTO {
    private int id;
    private String startTime;
    private String endTime;
    private double totalCost;
    private UserInRideDTO driver;
    private List<UserInRideDTO> passengers;
    private int estimatedTimeInMinutes;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private List<LocationDTO> locations;
    private double distance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public UserInRideDTO getDriver() {
        return driver;
    }

    public void setDriver(UserInRideDTO driver) {
        this.driver = driver;
    }

    public List<UserInRideDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<UserInRideDTO> passengers) {
        this.passengers = passengers;
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public List<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDTO> locations) {
        this.locations = locations;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getRouteDepartureDestinationTitle(){
        String departureAddress = this.getLocations().get(0).getDeparture().getAddress();
        String destinationAddress = this.getLocations().get(0).getDestination().getAddress();
        return departureAddress+ " -> " + destinationAddress;
    }

    public String getStartDateTextView(){
        String time = this.getStartTime().split("T")[1].split(":")[0] + ":" + this.getStartTime().split("T")[1].split(":")[1];
        String day = this.getStartTime().split("T")[0].split("-")[2];
        String month = this.getStartTime().split("T")[0].split("-")[1];
        String year = this.getStartTime().split("T")[0].split("-")[0];
        return "Date: " + day + "." + month + "." + year + ". " + time;
    }
}
