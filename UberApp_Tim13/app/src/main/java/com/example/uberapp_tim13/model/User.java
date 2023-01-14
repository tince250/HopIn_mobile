package com.example.uberapp_tim13.model;

public class User {
    private int id;
    private String name;
    private String surName;
    private String phone;
    private String email;
    private String password;
    private String street;
    private String city;
    private String role;
    private String streetNum;
    private CreditCard card;

    public User(){
        super();
    }



    public User(int id, String name, String surName, String phone, String email, String password,
                String address, String city, String role, String houseNum, CreditCard card) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.street = address;
        this.city = city;
        this.role = role;
        this.streetNum = houseNum;
        this.card = card;
    }


    public String getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public CreditCard getCard() {
        return card;
    }
}
