package com.example.uberapp_tim13.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class WorkingHoursStartDTO implements Serializable {
	
	private LocalDateTime start;

	public WorkingHoursStartDTO() {
	}

	public WorkingHoursStartDTO(LocalDateTime start) {
		super();
		this.start = start;
	}
  
	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

}
