package com.example.uberapp_tim13.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;


public class WorkingHoursEndDTO implements Serializable {

//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
	private LocalDateTime end;

	public WorkingHoursEndDTO() {
	}

	public WorkingHoursEndDTO(LocalDateTime end) {
		super();
		this.end = end;
	}

	public LocalDateTime getEnd() {
		return end;        
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

}
