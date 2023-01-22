package com.example.uberapp_tim13.dtos;

import com.example.uberapp_tim13.model.Ride;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllPassengerRidesDTO implements Serializable {
    @SerializedName("totalCount")
    @Expose
    private int totalCount;
    @SerializedName("results")
    @Expose
    private List<RideReturnedDTO> results;

    @Override
    public String toString() {
        return "AllPassengerRidesDTO{" +
                "totalCount=" + totalCount +
                ", results=" + results +
                '}';
    }

    public List<RideReturnedDTO> getResults(){
        return results;
    }
}
