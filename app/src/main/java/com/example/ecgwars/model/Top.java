package com.example.ecgwars.model;

public class Top {

    public String getPlace() {
        return place;
    }

    private String place;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String score;

    public Top() {
    }

    public Top(String place, String firstName, String lastName, String imageUrl, String score) {
        this.place = place;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.score = score;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
