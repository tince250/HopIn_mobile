package com.example.uberapp_tim13.items;

public class RatingItem {

    private String comment;
    private float grade;
    private User passenger;

    public RatingItem(String comment, float grade, User passenger) {
        this.comment = comment;
        this.grade = grade;
        this.passenger = passenger;
    }

    public String getComment() {
        return comment;
    }

    public float getGrade() {
        return grade;
    }

    public User getPassenger() {
        return passenger;
    }
}
