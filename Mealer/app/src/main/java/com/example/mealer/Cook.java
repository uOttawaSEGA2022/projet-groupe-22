package com.example.mealer;

public class Cook extends User {

    private String name;
    private String lastName;

    private String address;

    public Cook() {
        super.setRole("Cook");
    }

    public String getName(){
        return this.name;
    }

    public String getLastName(){
        return this.lastName;
    }

    public String getAddress(){
        return this.address;
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

}
