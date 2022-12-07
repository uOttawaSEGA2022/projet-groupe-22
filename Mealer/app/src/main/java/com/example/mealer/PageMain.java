package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.Locale;
import java.util.Map;


public class PageMain extends AppCompatActivity {
    FirebaseUser fUser;
    FirebaseAuth fAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    Button addComplaintBtn;
    Button signOutBtn;
    Button search;

    ListView listView;

    //meals attributes
    DatabaseReference mealsChefReference;
    DatabaseReference mealsReference;
    List<Meal> meals;
    ListView mealsListView;

    SearchView mealsListViewSearch;
    ArrayAdapter<String> arrayAdapter;

    DatabaseReference mealsSearchReference;
    List<String> mealsSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_main);

        initSearchWidgets();
        viewMealsList();
        //setting the meals attributes
        mealsListView = (ListView) findViewById(R.id.mealsListView);
        mealsSearch = new ArrayList<>();
        meals = new ArrayList<>();

        addComplaintBtn = findViewById(R.id.addComplaintBtn);
        addComplaintBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addComplaint();
            }
        });

        signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fAuth.signOut();
                finish();
                Intent intent = new Intent(PageMain.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });


        mealsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Meal meal = meals.get(position);
                return true;
            }
        });
    }



    public void initSearchWidgets(){

        mealsListViewSearch = (SearchView) findViewById(R.id.mealsListViewSearch);

        mealsListViewSearch.setQueryHint("Search by meal's name or type");

        mealsListViewSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //called when the user presses enter
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            //called to search for what the client wrote
            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Meal> filteredMeals = new ArrayList<Meal>();
                for(Meal meal : meals){
                    if((meal.getMealName().toLowerCase().contains(s.toLowerCase())) ||
                         meal.getMealType().toLowerCase().contains(s.toLowerCase())||
                          meal.getGastronomyType().toLowerCase().contains(s.toLowerCase())){
                        filteredMeals.add(meal);
                    }
                }

                MealsList adapter = new MealsList(PageMain.this, filteredMeals);
                //attaching adapter to the listview
                mealsListView.setAdapter(adapter);
                return false;
            }
        });

    }

    public void addComplaint() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_complaint, null);
        dialogBuilder.setView(dialogView);

        final EditText ComplaintChefName = (EditText) dialogView.findViewById(R.id.ComplaintChefName);
        final EditText complaintDate = (EditText) dialogView.findViewById(R.id.complaintDate);
        final EditText complaintText = (EditText) dialogView.findViewById(R.id.complaintText);
        final Button addComplaintBtn = (Button) dialogView.findViewById(R.id.addComplaintBtn);

        dialogBuilder.setTitle("Add complaint");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        addComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {

                    String chefName = ComplaintChefName.getText().toString().trim();
                    String date = complaintDate.getText().toString().trim();
                    String text = complaintText.getText().toString().trim();

                    DatabaseReference complaintReference = FirebaseDatabase.getInstance().getReference().child("complaints");


                    //checking if the value is provided
                    if (!(TextUtils.isEmpty(chefName) & TextUtils.isEmpty(date) & TextUtils.isEmpty(text))) {

                        //getting a unique id using push().getKey() method
                        //it will create a unique id and we will use it as the Primary key for our Complaint
                        String id = reference.push().getKey();

                        //creating a Complaint Object
                        Complaint complaint = new Complaint(id, chefName, date, text);

                        //saving the complaint
                        complaintReference.child(id).setValue(complaint);

                        //setting edittext to blank again
                        ComplaintChefName.setText("");
                        complaintDate.setText("");
                        complaintText.setText("");

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
        Toast.makeText(this, "Complaint added", Toast.LENGTH_LONG).show();
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
                String name = snapshot.getValue(String.class);
                Toast.makeText(PageMain.this, "Welcome, " + name + "!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void viewMealsList() {

        mealsReference = FirebaseDatabase.getInstance().getReference().child("meals");
        mealsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                meals.clear();

                //listening through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot ds : postSnapshot.getChildren()) {
                        if (ds.child("display").getValue(Boolean.class)) {
                            Meal meal = ds.getValue(Meal.class);
                            //adding product to the list
                            meals.add(meal);
                        }
                    }
                }
                //creating adapter
                MealsList mealsAdapter = new MealsList(PageMain.this, meals);
                //attaching adapter to the listview
                mealsListView.setAdapter(mealsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
