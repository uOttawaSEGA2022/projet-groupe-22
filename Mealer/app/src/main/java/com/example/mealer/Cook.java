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
        super(email, password);
        this.name = name;
        this.lastName = lastName;
        this.address = address;
    }
    /**
     * @return
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return
     */
    public String getLastName(){
        return this.lastName;
    }
    /**
     * @return
     */
    public String getAddress(){
        return this.address;
    }

    /**
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @param lastName
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setAddress(String address){
        this.address = address;
    }

}
