package com.example.mealer;

public class User {
    
    private String email;
    private String password;
    
    /**
     * @param email
     * @param password
     */
    public User (String email , String password){
        this.email = email;
        this.password = password;

    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }



    
}