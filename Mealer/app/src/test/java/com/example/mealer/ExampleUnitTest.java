package com.example.mealer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {

        Cook cook1 = new Cook();
        cook1.setName("Zach");

        Meal meal1 = new Meal();
        meal1.setMealName("French fries");

        @Test
        public void cookGetName_isFunctional() {assertEquals("Zach",cook1.getName());}

        @Test
        public void mealGetName_isFunctional() {assertEquals("French fries",meal1.getMealName());}

        @Test
        public void mealSetPrice_isFunctional() {
                meal1.setPrice("69"); assertEquals("69", meal1.getPrice());
        }

        @Test
        public void mealSetDisplay_isFunctional() {
                meal1.setDisplay(true); assertEquals(true, meal1.getDisplay()); }

    }
}