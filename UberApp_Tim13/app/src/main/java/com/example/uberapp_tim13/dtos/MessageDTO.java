package com.example.uberapp_tim13.dtos;


import java.io.Serializable;

public class MessageDTO implements Serializable {
	
	int receiverId;
	String message;
	String type;
	int rideId;
	
	public MessageDTO(int receiverId, String message, String type, int rideId) {
		super();
		this.receiverId = receiverId;
		this.message = message;
		this.type = type;
		this.rideId = rideId;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRideId() {
		return rideId;
	}

	public void setRideId(int rideId) {
		this.rideId = rideId;
	}
}
