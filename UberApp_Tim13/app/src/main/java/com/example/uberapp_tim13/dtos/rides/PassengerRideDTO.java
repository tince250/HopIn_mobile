package com.example.uberapp_tim13.dtos.rides;

import com.example.uberapp_tim13.dtos.locations.LocationDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassengerRideDTO {
    @SerializedName("totalCount")
    @Expose
    private int id;
    @SerializedName("totalCount")
    @Expose
    private String startTime;
    @SerializedName("totalCount")
    @Expose
    private String endTime;
    @SerializedName("totalCount")
    @Expose
    private double totalCost;
    @SerializedName("totalCount")
    @Expose
    private UserInRideDTO driver;
    @SerializedName("totalCount")
    @Expose
    private List<UserInRideDTO> passengers;
    @SerializedName("totalCount")
    @Expose
    private int estimatedTimeInMinutes;
    @SerializedName("totalCount")
    @Expose
    private String vehicleType;
    @SerializedName("totalCount")
    @Expose
    private boolean babyTransport;
    @SerializedName("totalCount")
    @Expose
    private boolean petTransport;
    @SerializedName("totalCount")
    @Expose
    private List<LocationDTO> locations;
}
