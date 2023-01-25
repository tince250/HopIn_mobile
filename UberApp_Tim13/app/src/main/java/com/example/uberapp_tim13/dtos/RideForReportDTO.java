package com.example.uberapp_tim13.dtos;

import java.time.LocalDateTime;

public class RideForReportDTO {
    private String startTime;
    private String endTime;
    private double distance;
    private int estimatedTimeInMinutes;
    private double totalCost;

    public RideForReportDTO() {}

    public RideForReportDTO(String startTime, String endTime, double distance, int estimatedTimeInMinutes, double totalCost) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.totalCost = totalCost;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "RideForReportDTO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", distance=" + distance +
                ", estimatedTimeInMinutes=" + estimatedTimeInMinutes +
                ", totalCost=" + totalCost +
                '}';
    }
}
