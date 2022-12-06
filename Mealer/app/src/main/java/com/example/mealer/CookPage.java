package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    Button signOutBtn;
    
    DatabaseReference databaseMeals;
    DatabaseReference mealsReference;
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

        databaseMeals = FirebaseDatabase.getInstance().getReference();
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

        listViewMeals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Meal meal = meals.get(position);
                decisionMake(meal.getID(),meal.getChefUid());
                return true;
            }
        });
        signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fAuth.signOut();
                finish();
                Intent intent = new Intent(CookPage.this,
                        MainActivity.class);
                startActivity(intent);
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
                viewMealsList();
            }
        });
    }

    private void decisionMake(final String mealID, String chefUid){
        //TODO: have to create the alertdialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.chef_meal_actions, null);
        dialogBuilder.setView(dialogView);

        final Button buttonDeleteMeal = (Button) dialogView.findViewById(R.id.deleteMealBtn);
        final Button buttonDisplayMeal = (Button) dialogView.findViewById(R.id.displayMealBtn);

        dialogBuilder.setTitle("What would you like to do with this meal?");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("meals").child(chefUid).child(mealID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                buttonDeleteMeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean displaystatus = snapshot.child("display").getValue(Boolean.class);
                        if (displaystatus==false) {
                            deleteMealfromMenu(mealID, chefUid);
                            b.dismiss();
                        }else{
                            Toast.makeText(CookPage.this, "You cannot delete a meal that is currently on display!", Toast.LENGTH_LONG).show();
                            b.dismiss();
                        }
                    }
                });

                buttonDisplayMeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean displaystat = snapshot.child("display").getValue(Boolean.class);
                        if(displaystat==true){
                            Toast.makeText(CookPage.this, "This meal is already on display!", Toast.LENGTH_LONG).show();
                            b.dismiss();
                        }else{
                            addMealToDisplay(mealID,chefUid);
                            b.dismiss();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CookPage.this, "Hey that didn't work out, sorry",Toast.LENGTH_LONG).show();
            }
        });

    }//end of decisionMake method

    private void addMealToDisplay(String mealID, String chefUid){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(chefUid).child(mealID);


    }

    private void deleteMealfromMenu(final String mealID, final String chefUId){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("meals").child(chefUId).child(mealID);
        ref.removeValue();
        Toast.makeText(getApplicationContext(), "Meal Deleted", Toast.LENGTH_LONG).show();
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
        viewMealsList();
    }

    public void viewMealsList(){

        fUser = fAuth.getCurrentUser();

        mealsReference = FirebaseDatabase.getInstance().getReference().child("meals").child(fUser.getUid());
        mealsReference.addValueEventListener(new ValueEventListener() {
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
    });}
}
