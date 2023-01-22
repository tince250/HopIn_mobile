package com.example.uberapp_tim13.dtos;


import java.io.Serializable;

public class LocationDTO implements Serializable {

	private LocationNoIdDTO departure;
	private LocationNoIdDTO destination;

	public LocationDTO() {}

	public LocationDTO(LocationNoIdDTO departure, LocationNoIdDTO destinations) {
		super();
		this.departure = departure;
		this.destination = destinations;
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

	public void setDestinations(LocationNoIdDTO destination) {
		this.destination = destination;
	}

}
