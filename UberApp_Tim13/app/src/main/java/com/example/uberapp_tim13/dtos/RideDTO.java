package com.example.uberapp_tim13.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class RideDTO {
	private List<LocationDTO> locations = new ArrayList<LocationDTO>();
	private List<UserInRideDTO> passengers = new ArrayList<UserInRideDTO>();
	private String vehicleType;
	private boolean babyTransport;
	private boolean petTransport;
	private double distance;
	private double duration;
	private double price;
	private LocalDateTime scheduledTime;

	public RideDTO() {
	}

	public RideDTO(List<LocationDTO> locations, List<UserInRideDTO> passengers, String vehicleType,
			boolean babyTransport, boolean petTransport) {
		super();
		this.locations = locations;
		this.passengers = passengers;
		this.vehicleType = vehicleType;
		this.babyTransport = babyTransport;
		this.petTransport = petTransport;
	}

	public RideDTO(List<LocationDTO> locations, List<UserInRideDTO> passengers, String vehicleType,
			boolean babyTransport, boolean petTransport, double distance, double duration, double price,
			LocalDateTime scheduledTime) {
		super();
		this.locations = locations;
		this.passengers = passengers;
		this.vehicleType = vehicleType;
		this.babyTransport = babyTransport;
		this.petTransport = petTransport;
		this.distance = distance;
		this.duration = duration;
		this.price = price;
		this.scheduledTime = scheduledTime;
	}

	public List<LocationDTO> getLocations() {
		return locations;
	}

	public void setLocations(List<LocationDTO> locations) {
		this.locations = locations;
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

	public LocalDateTime getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(LocalDateTime scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

}
