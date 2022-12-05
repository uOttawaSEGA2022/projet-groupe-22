package com.example.mealer;

import com.google.firebase.database.DatabaseReference;

public class Administrator extends User{

    static DatabaseReference chefSuspended;

    public Administrator(){
        super.setRole("Admin");
    }
    //idea of using it to save the reference of the chef
/*
    public void saveSuspChefRef(DatabaseReference chefSuspended){
        this.chefSuspended = chefSuspended;
    }

    public static DatabaseReference getSuspChefRef(){
        return chefSuspended;
    }

 */

}
