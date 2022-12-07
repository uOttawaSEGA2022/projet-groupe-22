package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChefProfile extends AppCompatActivity {

    List<Meal> displayedmeals;
    ListView listViewDisplayedMeals;
    DatabaseReference databaseMeals;
    DatabaseReference mealsReference;

    FirebaseUser fUser;
    FirebaseAuth fAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_profile);

        displayedmeals = new ArrayList<>();
        databaseMeals = FirebaseDatabase.getInstance().getReference();

        listViewDisplayedMeals = (ListView) findViewById(R.id.listViewDisplayedMeals);

    }

    public void onStart() {
        super.onStart();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        String IDstring = fUser.getUid();
        viewMealsList();
    }

    public void viewMealsList(){

        fUser = fAuth.getCurrentUser();

        mealsReference = FirebaseDatabase.getInstance().getReference().child("meals").child(fUser.getUid());
        mealsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                displayedmeals.clear();

                //listening through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting products
                    Meal meal = postSnapshot.getValue(Meal.class);
                    //adding product to the list
                        if(meal.getDisplay()==true) {
                            displayedmeals.add(meal);
                        }
                }
                //creating adapter
                MealsList mealsAdapter = new MealsList(ChefProfile.this, displayedmeals);
                //attaching adapter to the listview
                listViewDisplayedMeals.setAdapter(mealsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}

}