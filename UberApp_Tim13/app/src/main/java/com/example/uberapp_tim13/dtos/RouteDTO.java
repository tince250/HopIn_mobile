package com.example.uberapp_tim13.dtos;

public class RouteDTO {
    private int id;
    private double distance;
    private LocationNoIdDTO departure;
    private LocationNoIdDTO destination;

    public RouteDTO(int id, double distance, LocationNoIdDTO departure, LocationNoIdDTO destination) {
        this.id = id;
        this.distance = distance;
        this.departure = departure;
        this.destination = destination;
    }

    public RouteDTO(RideReturnedDTO ride) {
        this.id = 0;
        this.distance = ride.getDistance();
        this.departure = ride.getLocations().get(0).getDeparture();
        this.destination = ride.getLocations().get(0).getDestination();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LocationNoIdDTO getDeparture() {
        return departure;
    }

    public void setDeparture(LocationNoIdDTO departure) {
        this.departure = departure;
    }

    public LocationNoIdDTO getDestination() {
        return destination;
    }

    public void setDestination(LocationNoIdDTO destination) {
        this.destination = destination;
    }
}
