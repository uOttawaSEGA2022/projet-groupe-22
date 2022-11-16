package com.example.mealer;

public class Client extends User{

    private String address;

    public Client() {
        super.setRole("Client");
    }

    public void setAddress(String address){this.address = address;}

}
