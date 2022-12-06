package com.example.mealer;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mealer.PageMain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Meal {
	
	private String chefUid ;
	private String mealName;
	private String mealType;
	private String gastronomyType;
	private String chefName;
	private double price;

	DatabaseReference mealReference;
	public Meal (){}

	public Meal (String chefUid,String mealName,String mealType,String gastronomyType,double price){
		this.chefUid = chefUid ;
		this.mealName = mealName ;
		this.mealType = mealType ;
		this.gastronomyType = gastronomyType ;
		this.price = price ;		

			mealReference = FirebaseDatabase.getInstance().getReference().child("users").child(chefUid).child("name");
        mealReference.addValueEventListener(new ValueEventListener() {
		@Override
		public void onDataChange(@NonNull DataSnapshot snapshot) {
			String chefName = snapshot.getValue(String.class);
			setChefName(chefName);
		}

		@Override
		public void onCancelled(@NonNull DatabaseError error) {
		}
	});
	}

	//Setters
	public void setChef (String chefUid){this.chefUid = chefUid ;}
	public void setChefName (String chefName){this.chefName = chefName ;}
	public void setMealName(String mealName){this.mealName = mealName ;}
	public void setMealType(String mealType){this.mealType = mealType ;}
	public void setGastronomyType(String gastronomyType){this.gastronomyType = gastronomyType ;}
	public void setPrice(double price){this.price = price ;}

	
	//Getters
	public String getChefUid(){return this.chefUid;}
	public String getChefName(){return this.chefName;}
	public String getMealName(){return this.mealName;}
	public String getMealType(){return this.mealType;}
	public String getGastronomyType(){return this.gastronomyType;}
	public double getPrice(){return this.price;}
}
