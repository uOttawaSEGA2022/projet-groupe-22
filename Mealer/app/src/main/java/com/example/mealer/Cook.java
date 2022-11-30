package com.example.mealer;

public class Cook extends User {

    private String name;
    private String lastName;

    private String address;

    private String description;

    //status of cook if suspended or not
    private boolean status;

    //for initial time of suspension
    //if not suspended, this is 0
    private long initialtime;

    //TODO: way of storing cheque

    public Cook() {
        super.setRole("Cook");
        status = false;
        initialtime = -1;
    }

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

    public void changeStatus() {this.status = true;}

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

    public boolean getStatus() {return this.status;}

    public long getInitialtime() {return initialtime;}



}
