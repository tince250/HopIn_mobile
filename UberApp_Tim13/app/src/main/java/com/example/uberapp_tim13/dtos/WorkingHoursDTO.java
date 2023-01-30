package com.example.uberapp_tim13.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class WorkingHoursDTO implements Serializable {
	private int id;
	private String start;
	private String end;

	public WorkingHoursDTO(int id, String start, String end) {
		super();
		this.id = id;
		this.start = start;
		this.end = end;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "WorkingHoursDTO{" +
				"id=" + id +
				", start=" + start +
				", end=" + end +
				'}';
	}
}
