package com.example.mealer;

//TODO: how i think this should work:
//TODO: admin class should have a reference to the database
//response : that is right. this is how it should work

import com.google.firebase.database.DatabaseReference;

public class Administrator extends User{

    static DatabaseReference chefSuspended;

    //  TODO: this is a work in progresss
    public Administrator(){
        super.setRole("Admin");
    }
    //TODO idea of using it to save the reference of the chef
/*
    public void saveSuspChefRef(DatabaseReference chefSuspended){
        this.chefSuspended = chefSuspended;
    }

    public static DatabaseReference getSuspChefRef(){
        return chefSuspended;
    }

 */

}
