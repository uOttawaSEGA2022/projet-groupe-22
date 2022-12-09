package com.example.mealer.model;

import com.example.mealer.Meal;
import com.example.mealer.MealsList;

public class MealModel extends Meal {
    private String key, mealName, chefName, price, gastronomyType, type;

    public MealModel() {

    }


    public void setKey(String key) {this.key = key;}
    public void setMealName(String mealName) {this.mealName = mealName;}
    public void setChefName(String chefName) {this.chefName = chefName;}
    public void setPrice(String price) {this.price = price;}
    public void setGastronomyType(String gastronomyType) {this.gastronomyType = price;}
    public void setType(String type) {this.type = type;}

    public String getKey() {return key;}
    public String getMealName() {return mealName;}
    public String getChefName() {return chefName;}
    //public String getPrice() {return String.valueOf(price);}
    public String getGastronomyType() {return gastronomyType;}
    public String getType() {return type;}

}
