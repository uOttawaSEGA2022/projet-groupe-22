package com.example.mealer.listener;

import com.example.mealer.Meal;
import com.example.mealer.model.MealModel;

import java.util.List;

public interface IMealLoadListener {

    void onMealLoadSuccess(List<Meal> mealModelList);
    void onMealLoadFailed (String meassage);
}
