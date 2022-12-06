package com.example.mealer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class MealsList extends ArrayAdapter<Meal> {

    private Activity context;
    List<Meal> meals;

    public MealsList(Activity context, List<Meal> meals) {
        super(context, R.layout.activity_meals_list, meals);
        this.context = context;
        this.meals = meals;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_meals_list, null, true);

        TextView textViewChefName = (TextView) listViewItem.findViewById(R.id.textViewChefName);
        TextView textViewMealName = (TextView) listViewItem.findViewById(R.id.textViewMealName);
        TextView textViewMealType = (TextView) listViewItem.findViewById(R.id.textViewMealType);
        TextView textPrice = (TextView) listViewItem.findViewById(R.id.textPrice);
        TextView textViewGastronomyType = (TextView) listViewItem.findViewById(R.id.textViewGastronomyType);

        Meal meal = meals.get(position);
        textViewChefName.setText(meal.getChefName());
        textViewMealName.setText(meal.getMealName());
        textViewMealType.setText(meal.getMealType());
        textPrice.setText((String.valueOf(meal.getPrice()))+" CAD");
        textViewGastronomyType.setText(meal.getGastronomyType());

        return listViewItem;
    }
}
