package com.example.mealer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button loginbutton;
    private Button registerbutton;
    private EditText inputPassword, inputEmail;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();



        //creating the variables for the user inputs
        inputEmail = (EditText) findViewById(R.id.inputemail);
        inputPassword = (EditText) findViewById(R.id.inputpassword);


        loginbutton = (Button) findViewById(R.id.loginbutton); //initialize
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: put login verification here
                onLoginButtonClicked(v); //sends to main page
            }
        });

        registerbutton = (Button) findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterOptions();
            }
        });
    }

    public void openRegisterOptions(){
        Intent intent = new Intent(this,RegisterOptions.class);
        startActivity(intent);
    }

    private boolean verifyInputs(String inputemail,String inputpassword ){

        //Creating the error messages
        if (inputemail.isEmpty()) {
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputemail).matches()) {
            inputEmail.setError("Please provide a valid email");
            inputEmail.requestFocus();
            return false;
        }

        if (inputpassword.isEmpty()) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return false;
        }

        if (inputpassword.length() < 6) {
            inputPassword.setError("Password should not be less than 6 characters");
            inputPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void onLoginButtonClicked(View view) {
        String inputemail = inputEmail.getText().toString().trim();
        String inputpassword = inputPassword.getText().toString().trim();
        if (!verifyInputs(inputemail, inputpassword)) {
            return;
        }else{
            performLogin();
        }
         //sends to main page
    }

    private void performLogin(){
        String inputemail = inputEmail.getText().toString().trim();
        String inputpassword = inputPassword.getText().toString().trim();
        fAuth.signInWithEmailAndPassword(inputemail,inputpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //TODO: make this so it opens a specific activity depending on what type of user it is
                    String id = fUser.getUid();

                    String[] roleholder = new String[1];


                    database = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("role");
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String role = snapshot.getValue(String.class);
                            System.out.println(role);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    String usertype = roleholder[0];
                    System.out.println(usertype);


                    if(usertype.equals("Client")) {
                        Intent intent = new Intent(MainActivity.this, PageMain.class);
                        startActivity(intent);
                    }
                    else if(usertype.equals("Administrator")){
                        //TODO:admin page
                    }
                    else if(usertype.equals("Cook")){
                        //TODO: cook page
                    }
                    Toast.makeText(MainActivity.this, "Successfully logged in! Welcome",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Login unsuccessful, try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


