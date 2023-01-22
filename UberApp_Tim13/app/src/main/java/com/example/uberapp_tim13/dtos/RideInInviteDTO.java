package com.example.uberapp_tim13.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RideInInviteDTO implements Serializable {
    private Integer id;
    private LocationNoIdDTO pickup;
    private LocationNoIdDTO destination;
    private List<UserInRideDTO> passengers = new ArrayList<UserInRideDTO>();
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private double distance;
    private double duration;
    private double price;
    private String scheduledTime;

    public RideInInviteDTO(LocationNoIdDTO pickup, LocationNoIdDTO destination, List<UserInRideDTO> passengers, String vehicleType, boolean babyTransport, boolean petTransport, double distance, double duration, double price, String scheduledTime) {
        this.pickup = pickup;
        this.destination = destination;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.distance = distance;
        this.duration = duration;
        this.price = price;
        this.scheduledTime = scheduledTime;
    }

    public RideInInviteDTO(Integer id, LocationNoIdDTO pickup, LocationNoIdDTO destination, List<UserInRideDTO> passengers, String vehicleType, boolean babyTransport, boolean petTransport, double distance, double duration, double price, String scheduledTime) {
        this.id = id;
        this.pickup = pickup;
        this.destination = destination;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.distance = distance;
        this.duration = duration;
        this.price = price;
        this.scheduledTime = scheduledTime;
    }

    public RideInInviteDTO(RideDTO rideInCreation) {
        this.id = null;
        this.pickup = rideInCreation.getLocations().get(0).getDeparture();
        this.destination = rideInCreation.getLocations().get(0).getDestination();
        this.passengers = rideInCreation.getPassengers();
        this.vehicleType = rideInCreation.getVehicleType();
        this.babyTransport = rideInCreation.isBabyTransport();
        this.petTransport = rideInCreation.isPetTransport();
        this.distance = rideInCreation.getDistance();
        this.duration = rideInCreation.getDuration();
        this.price = rideInCreation.getPrice();
        this.scheduledTime = rideInCreation.getScheduledTime();
    }

    public RideInInviteDTO(RideReturnedDTO ride) {
        this.id = ride.getId();
        this.pickup = ride.getLocations().get(0).getDeparture();
        this.destination = ride.getLocations().get(0).getDestination();
        this.passengers = ride.getPassengers();
        this.vehicleType = ride.getVehicleType();
        this.babyTransport = ride.isBabyTransport();
        this.petTransport = ride.isPetTransport();
        this.distance = ride.getDistance();
        this.duration = ride.getEstimatedTimeInMinutes();
        this.price = ride.getTotalCost();
        this.scheduledTime = ride.getScheduledTime();
    }

    public LocationNoIdDTO getPickup() {
        return pickup;
    }

    public void setPickup(LocationNoIdDTO pickup) {
        this.pickup = pickup;
    }

    public LocationNoIdDTO getDestination() {
        return destination;
    }

    public void setDestination(LocationNoIdDTO destination) {
        this.destination = destination;
    }

    public List<UserInRideDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<UserInRideDTO> passengers) {
        this.passengers = passengers;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
