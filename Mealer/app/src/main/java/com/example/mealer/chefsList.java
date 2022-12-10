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

import com.example.mealer.model.CartModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class chefsList extends  ArrayAdapter<CartModel> {

    private Activity context;
    List<CartModel> meals;

    DatabaseReference cartMealReference;

    public chefsList(Activity context, List<CartModel> meals) {
        super(context, R.layout.activity_chefs_list, meals);
        this.context = context;
        this.meals = meals;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_chefs_list, null, true);

       @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textViewChefName = (TextView) listViewItem.findViewById(R.id.textChefName);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textViewMealName = (TextView) listViewItem.findViewById(R.id.textMealName);

        CartModel meal = meals.get(position);
        cartMealReference = FirebaseDatabase.getInstance().getReference().child("users").child(meal.getChefName()).child("name");
        cartMealReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chefName = snapshot.getValue(String.class).toString();
                //setChefName(chefName);
                textViewChefName.setText("Chef Name : "+chefName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        textViewMealName.setText("Meal Name : "+meal.getName());

        return listViewItem;
    }
}
