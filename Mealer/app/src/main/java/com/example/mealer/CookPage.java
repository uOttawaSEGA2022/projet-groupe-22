package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CookPage extends AppCompatActivity {

    TextView welcomingTag;
    Button addMealBtn;

    DatabaseReference databaseMeals;
    List<Meal> meals;
    ListView listViewMeals;

    FirebaseUser fUser;
    FirebaseAuth fAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_page);

        databaseMeals = FirebaseDatabase.getInstance().getReference("meals");
        meals = new ArrayList<>();

        listViewMeals = (ListView) findViewById(R.id.listViewMeals);
        welcomingTag = (TextView) findViewById(R.id.welcomingTag);
        addMealBtn = (Button) findViewById(R.id.addMealBtn);
        addMealBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addMeal();
            }
        });

    }

    public void addMeal() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_meal, null);
        dialogBuilder.setView(dialogView);

        final EditText mealName = (EditText) dialogView.findViewById(R.id.mealName);
        final EditText mealType = (EditText) dialogView.findViewById(R.id.mealType);
        final EditText mealGastronomyType = (EditText) dialogView.findViewById(R.id.mealGastronomyType);
        final EditText mealPrice = (EditText) dialogView.findViewById(R.id.mealPrice);
        final Button addMealBtn = (Button) dialogView.findViewById(R.id.addMealBtn);

        dialogBuilder.setTitle("Add meal");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        addMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {

                    String meal_name = mealName.getText().toString().trim();
                    String meal_type = mealType.getText().toString().trim();
                    String meal_gastronomy_type = mealGastronomyType.getText().toString().trim();
                    String meal_price = mealPrice.getText().toString().trim();

                    DatabaseReference mealsReference = FirebaseDatabase.getInstance().getReference().child("meals").child(fUser.getUid());

                    //checking if the value is provided
                    if (!(TextUtils.isEmpty(meal_name) & TextUtils.isEmpty(meal_type) &
                            TextUtils.isEmpty(meal_gastronomy_type) & TextUtils.isEmpty(meal_price))) {

                        //getting a unique id using push().getKey() method
                        //it will create a unique id and we will use it as the Primary key for our meal
                        String id = reference.push().getKey();

                        //creating a Product Object
                        Meal meal = new Meal(id, fUser.getUid(), meal_name, meal_type, meal_gastronomy_type, Double.parseDouble(meal_price));

                        //saving the product
                        mealsReference.child(id).setValue(meal);

                        //setting edittext to blank again
                        mealName.setText("");
                        mealType.setText("");
                        mealGastronomyType.setText("");
                        mealPrice.setText("");

                        //displaying a success toast
                        successToaster();
                        b.dismiss();
                    } else {
                        //if the values are not given displaying a toast
                        failingToaster();
                        b.dismiss();

                    }
                }
            }
        });
    }

    public void successToaster() {
        Toast.makeText(this, "Meal added", Toast.LENGTH_LONG).show();
    }

    public void failingToaster() {
        Toast.makeText(this, "Try again and fill all the fields with the needed info", Toast.LENGTH_LONG).show();
    }


    public void onStart() {
        super.onStart();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        String IDstring = fUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(IDstring).child("name");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String firstName = String.valueOf(reference.child("name"));

                String firstNAme = snapshot.getValue(String.class);

                welcomingTag.setText("Welcome  " + firstNAme + " !");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        databaseMeals.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                meals.clear();

                //listening through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting products
                    Meal meal = postSnapshot.getValue(Meal.class);
                    //adding product to the list
                    meals.add(meal);
                }
                //creating adapter
                MealsList mealsAdapter = new MealsList(CookPage.this, meals);
                //attaching adapter to the listview
                listViewMeals.setAdapter(mealsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
