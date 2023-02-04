package com.example.uberapp_tim13.dtos;

public class RideOfferResponseDTO {
    private boolean response;
    private RideReturnedDTO ride;

    public RideOfferResponseDTO(boolean response, RideReturnedDTO ride) {
        super();
        this.response = response;
        this.ride = ride;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public RideReturnedDTO getRide() {
        return ride;
    }

    public void setRide(RideReturnedDTO ride) {
        this.ride = ride;
    }

    @Override
    public String toString() {
        return "RideOfferResponseDTO [response=" + response + ", ride=" + ride + "]";
    }

}
