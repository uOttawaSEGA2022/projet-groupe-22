package com.example.mealer;

public class User {
    
    private String email;
    private String password;
    private String role; //easier to differentiate

    /**
     * make everything a setter here, it's easier
     */
    public User (){}

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public String getRole() {return this.role;}

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setRole(String role) {this.role = role;}


    
}