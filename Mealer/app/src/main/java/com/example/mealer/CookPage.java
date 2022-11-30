package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
                String firstName = String.valueOf(reference.child("name"));
                String lastName = String.valueOf(reference.child("lastName"));

                //String firstNAme = snapshot.getValue(String.class);

                viewFirstName.setText(firstName);
                viewLastName.setText(lastName);
                welcomingTag.setText("Welcome" + firstName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}
