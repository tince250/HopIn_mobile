package com.example.uberapp_tim13.dtos;

import java.io.Serializable;

public class LocationNoIdDTO implements Serializable {
	private String address;
	private double latitude;
	private double longitude;
	
	public LocationNoIdDTO() {}

	public LocationNoIdDTO(String address, double latitude, double longitude) {
		super();
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "LocationNoIdDTO{" +
				"address='" + address + '\'' +
				", latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}
}
