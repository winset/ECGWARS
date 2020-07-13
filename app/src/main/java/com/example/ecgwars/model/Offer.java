package com.example.ecgwars.model;

public class Offer {

    private String number;
    private String text;


    public Offer(String number, String text) {
        this.number = number;
        this.text = text;
    }
    public String getNumber() {
        return number;
    }
    public String getText() {
        return text;
    }
}
