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
    private long susTime;

    //TODO: way of storing cheque

    public Cook() {
        super.setRole("Cook");
        status = 0;
        susTime = 0;
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
    public void setSusTime(Long susTime) { this.susTime = susTime;}

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
    public Long getSusTime() {return susTime;}

}
