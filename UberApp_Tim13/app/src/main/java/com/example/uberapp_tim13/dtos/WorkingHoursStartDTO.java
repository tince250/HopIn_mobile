package com.example.uberapp_tim13.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class WorkingHoursStartDTO implements Serializable {
	
	private String start;

	public WorkingHoursStartDTO() {
	}

	public WorkingHoursStartDTO(String start) {
		super();
		this.start = start;
	}
  
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

}
