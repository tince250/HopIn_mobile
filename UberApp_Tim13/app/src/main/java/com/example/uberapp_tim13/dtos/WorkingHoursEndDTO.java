package com.example.uberapp_tim13.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;


public class WorkingHoursEndDTO implements Serializable {

//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
	private String end;

	public WorkingHoursEndDTO() {
	}

	public WorkingHoursEndDTO(String end) {
		super();
		this.end = end;
	}

	public String getEnd() {
		return end;        
	}

	public void setEnd(String end) {
		this.end = end;
	}

}
