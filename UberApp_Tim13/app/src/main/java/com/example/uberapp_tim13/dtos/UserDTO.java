package com.example.uberapp_tim13.dtos;


import com.example.uberapp_tim13.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDTO implements Serializable {
	@SerializedName("id")
	@Expose
	private int id;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("surname")
	@Expose
	private String surname;
	@SerializedName("profilePicture")
	@Expose
	private String profilePicture;
	@SerializedName("telephoneNumber")
	@Expose
	private String telephoneNumber;
	@SerializedName("email")
	@Expose
	private String email;
	@SerializedName("address")
	@Expose
	private String address;
	@SerializedName("password")
	@Expose
	private String password;
	
	public UserDTO() {}

	public UserDTO(int id, String name, String surname, String profilePicture, String telephoneNumber,
			String email, String address, String password) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.profilePicture = profilePicture;
		this.telephoneNumber = telephoneNumber;
		this.email = email;
		this.address = address;
		this.password = password;
	}


	public UserDTO(User user) {
		//TODO: make this work
		super();
		this.id = user.getId();
		this.name = user.getName();
		this.surname = user.getSurName();
		this.profilePicture = null;
		this.telephoneNumber = user.getPhone();
		this.email = user.getEmail();
		this.address = null;
		this.password = user.getPassword();
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserDTO{" +
				"id=" + id +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", profilePicture='" + profilePicture + '\'' +
				", telephoneNumber='" + telephoneNumber + '\'' +
				", email='" + email + '\'' +
				", address='" + address + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
