package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.example.mealer.model.CartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ratingChefs extends AppCompatActivity {

    ListView listViewChefs;
    FirebaseUser fUser;
    FirebaseAuth fAuth;
    DatabaseReference mealsReference;
    DatabaseReference  ratingChefs;
    List<CartModel> mealsDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_chefs);
        //setting the auth
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
            //cartmeals reference
        mealsReference = FirebaseDatabase.getInstance().getReference("cart");
        //create the list
        mealsDetails = new ArrayList<>();
        //set the click listener to the list items
        listViewChefs = (ListView) findViewById(R.id.listViewChefs);
        listViewChefs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CartModel meal = mealsDetails.get(position);
                setRatingDialog(meal);
                return true;
            }
        });
    }

    private void setRatingDialog(CartModel cartMeal) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.set_rating, null);
            dialogBuilder.setView(dialogView);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText editTextRate = (EditText) dialogView.findViewById(R.id.editTextRating);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateRating);

            dialogBuilder.setTitle("Set the rating for "+cartMeal.getChefName());
            final AlertDialog b = dialogBuilder.create();
            b.show();

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ratetxt = editTextRate.getText().toString().trim();
                    double rate = Double.parseDouble(String.valueOf(ratetxt));
                    if (!TextUtils.isEmpty(ratetxt)) {
                        setChefRating(cartMeal.getChefName(), rate);
                        b.dismiss();
                    }
                }
            });

    }

    //set the list
    @Override
    protected void onStart() {
        super.onStart();
        mealsReference.child(fUser.getUid()).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                //clearing the previous artist list
                mealsDetails.clear();
                //listening through all the nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //getting products
                        CartModel product = postSnapshot.getValue(CartModel.class);
                        if (product.isStatus()){
                            mealsDetails.add(product);}
                    }
                    //creating adapter
                    chefsList productsAdapter = new chefsList(ratingChefs.this, mealsDetails);
                    //attaching adapter to the listview
                    listViewChefs.setAdapter(productsAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError){
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setChefRating(String chefUid, Double rate) {

        ratingChefs = FirebaseDatabase.getInstance().getReference().child("users").child(chefUid);

        ratingChefs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double oldRate = dataSnapshot.child("rating").getValue(Double.class);
                ratingChefs.child("rating").setValue(oldRate+rate);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}