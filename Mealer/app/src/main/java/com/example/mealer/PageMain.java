package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PageMain extends AppCompatActivity {
    FirebaseUser fUser;
    FirebaseAuth fAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_main);
    }
    public void onStart() {
        super.onStart();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        String IDstring = fUser.getUid();

        String name = String.valueOf(reference.child("users").child(IDstring).child("name").get());
        System.out.println(name);

        Toast.makeText(PageMain.this,"Welcome,"+ name +"!",Toast.LENGTH_SHORT).show();

    }
}