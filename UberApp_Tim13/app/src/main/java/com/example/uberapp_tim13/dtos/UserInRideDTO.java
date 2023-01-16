package com.example.uberapp_tim13.dtos;

import com.example.uberapp_tim13.model.User;

import java.io.Serializable;

public class UserInRideDTO implements Serializable {
	private int id;
	private String email;
	
	public UserInRideDTO() {}
	
	public UserInRideDTO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
	}

	public UserInRideDTO(int id, String email) {
		super();
		this.id = id;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
