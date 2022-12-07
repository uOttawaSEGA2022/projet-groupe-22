package com.example.mealer;

public class Cook extends User {

    private String name;
    private String lastName;

    private String address;

    private String description;

    //status of cook if suspended or not
    private int status;

    //for initial time of suspension
    //if not suspended, this is 0
    //private long initialtime;
    private String susTime;

    //rating set as 2.5 when creating the chefs' profile
    private Double rating;

    //TODO: way of storing cheque

    public Cook() {
        super.setRole("Cook");
        status = 0;
        susTime = null;
        rating = 2.5;
    }

    //setters
    public void setName(String name){
        this.name = name;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setDescription(String description) { this.description = description; }
    public void changeStatus_sus_eter() {this.status = 2;}
    public void changeStatus_sus_temp() {this.status = 1;}
    public void setSusTime(String susTime) { this.susTime = susTime;}
    public void setRating(Double rating) {this.rating = rating;}

    //getters
    public String getName(){
        return this.name;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getAddress(){
        return this.address;
    }
    public String getDescription() {return this.description; }
    public int getStatus() {return this.status;}
    public String getSusTime() {return susTime;}
    public Double getRating() {return rating;}

}
