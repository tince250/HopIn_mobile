package com.example.uberapp_tim13.model;

import java.util.List;

public class Ride {

    private User driver;
    private List<User> passengers;
    private String startTime;
    private String endTime;
    private String pickUpLocation;
    private List<String> stops;
    private String destination;
    private float distance;
    private double price;
    private List<Rating> ratings;

    public Ride(User driver, List<User> passengers, String startTime, String endTime, String pickUpLocation, List<String> stops, String destination, float distance, double price, List<Rating> ratings) {
        this.driver = driver;
        this.passengers = passengers;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pickUpLocation = pickUpLocation;
        this.stops = stops;
        this.destination = destination;
        this.distance = distance;
        this.price = price;
        this.ratings = ratings;
    }

    public User getDriver() {
        return driver;
    }

    public List<User> getPassengers() {
        return passengers;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public List<String> getStops() {
        return stops;
    }

    public String getDestination() {
        return destination;
    }

    public float getDistance() {
        return distance;
    }

    public double getPrice() {
        return price;
    }

    public List<Rating> getRatings() {
        return ratings;
    }
}
