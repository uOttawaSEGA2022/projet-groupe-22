package com.example.mealer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button loginbutton;
    private Button registerbutton;
    private EditText inputPassword, inputEmail;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    DatabaseReference reference;
    DatabaseReference reference2;
    int status;


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
    //archiving the following method because it works on its own, but not in practice for some reason
    /*
    int checkStatus(String id){

        reference2 = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("status");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int status = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return status;
    }
     */

    private void performLogin(){
        String inputemail = inputEmail.getText().toString().trim();
        String inputpassword = inputPassword.getText().toString().trim();
        fAuth.signInWithEmailAndPassword(inputemail,inputpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    fUser = fAuth.getCurrentUser();
                    String id = fUser.getUid();


                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(id);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String usertype = snapshot.child("role").getValue(String.class);


                            if(usertype.equals("Client")) {
                                Intent intent = new Intent(MainActivity.this, PageMain.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Successfully logged in! Welcome",Toast.LENGTH_SHORT).show();
                            }
                            if(usertype.equals("Admin")){
                                Intent intent = new Intent(MainActivity.this, AdminPage.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Successfully logged in! Welcome",Toast.LENGTH_SHORT).show();
                            }
                            if(usertype.equals("Cook")){
                                //TODO: test --v
                                if (snapshot.child("status").getValue(Integer.class)==0){
                                    Intent intent = new Intent(MainActivity.this, CookPage.class);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "Successfully logged in! Welcome",Toast.LENGTH_SHORT).show();}
                                else if(snapshot.child("status").getValue(Integer.class)==1) {
                                    //take the suspension time from the database of the chef

                                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("users").child(id).child("susTime");
                                    reference3.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String chefsuspdate = snapshot.getValue(String.class);

                                            Calendar calendar = Calendar.getInstance();
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                            String currentdate = dateFormat.format(calendar.getTime());

                                            if(currentdate.equals(chefsuspdate)){
                                                //emptying suspension
                                                reference.child("status").setValue(0);
                                            }else {
                                            /*
                                            Date currentDate = new Date (date);
                                            Date suspDate = new Date (chefsuspdate);
                                            long diff = suspDate.getTime() - currentDate.getTime();
                                            long seconds = diff / 1000;
                                            long minutes = seconds / 60;
                                            long hours = minutes / 60;
                                            long days = (hours / 24) + 1;*/
                                                Toast.makeText(MainActivity.this, "Sorry you were suspended till " + chefsuspdate, Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Sorry you were suspended :(",Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Hey that didn't work out, sorry",Toast.LENGTH_LONG).show();
                        }
                    });

                }else{
                    Toast.makeText(MainActivity.this, "Login unsuccessful, try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
