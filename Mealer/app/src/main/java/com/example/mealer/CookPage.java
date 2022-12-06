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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CookPage extends AppCompatActivity {

    TextView viewFirstName;
    TextView viewLastName;
    TextView welcomingTag;
    Button addMealBtn;

    FirebaseUser fUser;
    FirebaseAuth fAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_page);

        viewFirstName = (TextView) findViewById(R.id.viewFirstName);
        viewLastName = (TextView) findViewById(R.id.viewLastName);
        welcomingTag = (TextView) findViewById(R.id.welcomingTag);
        addMealBtn = (Button) findViewById(R.id.addMealBtn);
        addMealBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {addMeal();}
        });

    }

    public void addMeal(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_meal, null);
        dialogBuilder.setView(dialogView);

        final EditText mealName = (EditText) dialogView.findViewById(R.id.mealName);
        final EditText mealType  = (EditText) dialogView.findViewById(R.id.mealType);
        final EditText mealGastronomyType  = (EditText) dialogView.findViewById(R.id.mealGastronomyType);
        final EditText mealPrice  = (EditText) dialogView.findViewById(R.id.mealPrice);
        final Button addMealBtn = (Button) dialogView.findViewById(R.id.addMealBtn);

        dialogBuilder.setTitle("Add complaint");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        addMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {{

                String meal_name = mealName.getText().toString().trim();
                String meal_type = mealType.getText().toString().trim();
                String meal_gastronomy_type = mealGastronomyType.getText().toString().trim();
                String meal_price = mealPrice.getText().toString().trim();

                DatabaseReference complaintReference = FirebaseDatabase.getInstance().getReference().child("meals");

                //checking if the value is provided
                if (!(TextUtils.isEmpty(meal_name) & TextUtils.isEmpty(meal_type) &
                        TextUtils.isEmpty(meal_gastronomy_type) & TextUtils.isEmpty(meal_price))) {

                    //getting a unique id using push().getKey() method
                    //it will create a unique id and we will use it as the Primary key for our meal
                    String id = reference.push().getKey();

                    //creating a Product Object
                    Meal meal = new Meal(id, fUser.getUid(), meal_name, meal_type, meal_gastronomy_type, Double.parseDouble(meal_price));

                    //saving the product
                    complaintReference.child(id).setValue(meal);

                    //setting edittext to blank again
                    mealName.setText("");
                    mealType.setText("");
                    mealGastronomyType.setText("");
                    mealPrice.setText("");

                    //displaying a success toast
                    successToaster();
                    b.dismiss();
                } else{
                    //if the values are not given displaying a toast
                    failingToaster();
                    b.dismiss();

                }
            }}
        });
    }

    public void successToaster(){Toast.makeText(this, "Meal added", Toast.LENGTH_LONG).show();}
    public void failingToaster(){Toast.makeText(this, "Try again and fill all the fields with the needed info", Toast.LENGTH_LONG).show();}



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

                viewFirstName.setText(firstNAme);
                welcomingTag.setText("Welcome  " + firstNAme +" !");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(IDstring).child("lastName");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String lastName = String.valueOf(reference.child("lastName"));

                String lastNAme = snapshot.getValue(String.class);

                viewLastName.setText(lastNAme);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }




}
