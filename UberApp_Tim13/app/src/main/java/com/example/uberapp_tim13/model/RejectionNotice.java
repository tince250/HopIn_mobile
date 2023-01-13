package com.example.uberapp_tim13.model;

import java.time.LocalDateTime;

public class RejectionNotice {

	private int id;
	private String reason;
	private LocalDateTime timeOfRejection;
	
	public RejectionNotice() {}
	
	public RejectionNotice(String reason) {
		this.id = 1;
		this.timeOfRejection = LocalDateTime.now();
		this.reason = reason;
	}

	public LocalDateTime getTimeOfRejection() {
		return timeOfRejection;
	}

	public void setTimeOfRejection(LocalDateTime timeOfRejection) {
		this.timeOfRejection = timeOfRejection;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	

}
