package com.example.uberapp_tim13.dtos;

import com.example.uberapp_tim13.model.RejectionNotice;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RideReturnedDTO implements Serializable {
	@SerializedName("id")
	@Expose
	private int id;
	@SerializedName("startTime")
	@Expose
	private String startTime;
	@SerializedName("endTime")
	@Expose
	private String endTime;
	@SerializedName("totalCost")
	@Expose
	private double totalCost;
	@SerializedName("driver")
	@Expose
	private UserInRideDTO driver;
	@SerializedName("passengers")
	@Expose
	private List<UserInRideDTO> passengers;
	@SerializedName("estimatedTimeInMinutes")
	@Expose
	private int estimatedTimeInMinutes;
	@SerializedName("vehicleType")
	@Expose
	private String vehicleType;
	@SerializedName("petTransport")
	@Expose
	private boolean petTransport;
	@SerializedName("babyTransport")
	@Expose
	private boolean babyTransport;
	@SerializedName("rejection")
	@Expose
	private RejectionNotice rejection;
	@SerializedName("locations")
	@Expose
	private List<LocationDTO> locations;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("scheduledTime")
	@Expose
	private String scheduledTime;
	private double distance;

	public RideReturnedDTO() {}

	public RideReturnedDTO(int id, String startTime, String endTime, double totalCost,
			UserInRideDTO driver, List<UserInRideDTO> passengers, int estimatedTimeInMinutes,
			String vehicleType, boolean petTransport, boolean babyTransport, RejectionNotice rejection,
			List<LocationDTO> locations, String status, String scheduledTime, double distance) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalCost = totalCost;
		this.driver = driver;
		this.passengers = passengers;
		this.estimatedTimeInMinutes = estimatedTimeInMinutes;
		this.vehicleType = vehicleType;
		this.petTransport = petTransport;
		this.babyTransport = babyTransport;
		this.rejection = rejection;
		this.locations = locations;
		this.status = status;
		this.scheduledTime = scheduledTime;
		this.distance = distance;
	}

	public RideReturnedDTO(RideInInviteDTO ride) {
		this.locations = new ArrayList<LocationDTO>();
		LocationDTO loc = new LocationDTO();
		loc.setDeparture(ride.getPickup());
		loc.setDestinations(ride.getDestination());
		locations.add(loc);
	}


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

	public void setTotalCost(double price) {
		this.totalCost = price;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isPetTransport() {
		return petTransport;
	}

	public void setPetTransport(boolean pet) {
		this.petTransport = pet;
	}

	public boolean isBabyTransport() {
		return babyTransport;
	}

	public void setBabyTransport(boolean baby) {
		this.babyTransport = baby;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public List<LocationDTO> getLocations() {
		return locations;
	}

	public void setLocations(List<LocationDTO> locations) {
		this.locations = locations;
	}

	public RejectionNotice getRejection() {
		return rejection;
	}

	public void setRejection(RejectionNotice rejection) {
		this.rejection = rejection;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
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

	public String getEndDateTextView(){
		String time = this.getEndTime().split("T")[1].split(":")[0] + ":" + this.getEndTime().split("T")[1].split(":")[1];
		String day = this.getEndTime().split("T")[0].split("-")[2];
		String month = this.getEndTime().split("T")[0].split("-")[1];
		String year = this.getEndTime().split("T")[0].split("-")[0];
		return "Date: " + day + "." + month + "." + year + ". " + time;
	}

	@Override
	public String toString() {
		return "RideReturnedDTO{" +
				"id=" + id +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", totalCost=" + totalCost +
				", driver=" + driver +
				", passengers=" + passengers +
				", estimatedTimeInMinutes=" + estimatedTimeInMinutes +
				", vehicleType='" + vehicleType + '\'' +
				", petTransport=" + petTransport +
				", babyTransport=" + babyTransport +
				", rejection=" + rejection +
				", locations=" + locations +
				", status='" + status + '\'' +
				", scheduledTime=" + scheduledTime +
				'}';
	}
}
