package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DisplayMealDetails extends ArrayAdapter<Meal> {

            private Activity context;
            List<Meal> meals;

            //chef details
            String chefAddress;
            int chefRating;

            public DisplayMealDetails(Activity context, List<Meal> meals) {
                super(context, R.layout.activity_display_meal_details, meals);
                this.context = context;
                this.meals = meals;

            }

    public void settingChefName(String chefUid){
        DatabaseReference chefReference = FirebaseDatabase.getInstance().getReference().child("users").child(chefUid);
        chefReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //listening through all the nodes
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            chefAddress = postSnapshot.child("address").getValue(String.class);
                            chefRating = postSnapshot.child("rating").getValue(Integer.class);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = context.getLayoutInflater();
                View listViewItem = inflater.inflate(R.layout.activity_display_meal_details, null, true);

                TextView textViewChefName = (TextView) listViewItem.findViewById(R.id.textViewChefName);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textViewChefAddress =
                                         (TextView) listViewItem.findViewById(R.id.textViewChefAddress);
                TextView textPrice = (TextView) listViewItem.findViewById(R.id.textPrice);
                TextView textViewChefRating = (TextView) listViewItem.findViewById(R.id.textViewChefRating);

                Meal meal = meals.get(position);
                settingChefName(meal.getChefUid());
                textViewChefName.setText(meal.getChefName());
                textViewChefAddress.setText(chefAddress);
                textPrice.setText(String.valueOf(meal.getPrice()));
                textViewChefRating.setText(String.valueOf(chefRating));

                return listViewItem;
            }
        }
