package com.example.uberapp_tim13.dtos.reviews;

import java.io.Serializable;

public class ReviewDTO implements Serializable {
    private int rating;
    private String comment;

    public ReviewDTO(){

    }
    public ReviewDTO(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
