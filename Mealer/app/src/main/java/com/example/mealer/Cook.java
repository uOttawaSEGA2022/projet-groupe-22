package com.example.mealer;

public class Cook extends User {

    private String name;
    private String lastName;

    private String address;


    /**
     * @param name
     * @param lastName
     * @param address
     * @param email
     * @param password
     */
    public Cook(String name, String lastName, String address, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        super.setEmail(email);
        super.setPassword(password);
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
