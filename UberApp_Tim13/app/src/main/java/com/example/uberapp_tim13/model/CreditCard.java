package com.example.uberapp_tim13.model;

public class CreditCard {
    private String type;
    private String number;
    private String cvc;
    private String year;
    private String month;

    public CreditCard(String type, String number, String cvc, String month, String year) {
        this.type = type;
        this.number = number;
        this.cvc = cvc;
        this.month = month;
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public String getCvc() {
        return cvc;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }
}
