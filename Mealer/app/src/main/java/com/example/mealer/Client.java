package com.example.mealer;

public class Client extends User{

    private String address;
    private String name;
    private String lastName;

    public Client() {
        super.setRole("Client");
    }

    public void setName(String name){
        this.name = name;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setAddress(String address){this.address = address;}

    public String getAddress() {return address;}

    public String getName() {return name;}

    public String getLastName() {return lastName;}



}
