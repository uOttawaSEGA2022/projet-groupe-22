package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//added implememntation

public class ClientRegisterPage extends AppCompatActivity implements View.OnClickListener {
    //*************************************************************************************************
    //deleted the instance and created it after as a textView instance
    //private Button donebutton;
    //*************************************************************************************************
    
    //*************************************************************************************************
    //added the instances for the edit texters
    private EditText editTextname, editTextLastName, editTextinputEmail, editTextpassword, editTextaddress, editTextcreditCard, editTextCVV, editTextExpiry;
    //added the instances for the textview
    //donebutton is the same one as for the old button above (deleted)
    private TextView creditText, donebutton;
    //*************************************************************************************************
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register_page);

        donebutton = (Button) findViewById(R.id.donebutton);
        //added the listener method
        donebutton.setOnClickListener(this);
        //deleted the method and re-created it again!
        /*
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
        */
        
        //*********************************************************************************************
        //added the view access to the edit texts 
        editTextname = (EditText) findViewById(R.id.inputname);
        editTextLastName = (EditText) findViewById(R.id.inputlastname);
        editTextinputEmail = (EditText) findViewById(R.id.inputemail);
        editTextpassword = (EditText) findViewById(R.id.inputpass);
        editTextaddress = (EditText) findViewById(R.id.address);
        editTextcreditCard = (EditText) findViewById(R.id.creditcard);
        editTextCVV = (EditText) findViewById(R.id.cvv);
        editTextExpiry = (EditText) findViewById(R.id.expiry);
        //*********************************************************************************************
        
    }
    
    private void registerUser(){
        //Creating the getters for the inputs
        
        String inputname=editTextname.getText().toString().trim();
        String inputlastname=editTextLastName.getText().toString().trim();
        String inputemail=editTextinputEmail.getText().toString().trim();
        String inputpass=editTextpassword.getText().toString().trim();
        String address=editTextaddress.getText().toString().trim();
        String creditcard=editTextcreditCard.getText().toString().trim();
        String cvv=editTextCVV.getText().toString().trim();
        String expiry=editTextExpiry.getText().toString().trim();
        
        //Creating the gerror messages
        
        if(inputname.isEmpty()){
            editTextname.setError("First name is required");
            editTextname.requestFocus();
            return;
        }
        if(inputlastname.isEmpty()){
            editTextLastName.setError("Last name is required");
            editTextLastName.requestFocus();
            return;
        }
        if(inputemail.isEmpty()){
            editTextinputEmail.setError("Email is required");
            editTextinputEmail.requestFocus();
            return;
        }
        if(inputpass.isEmpty()){
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            return;
        }
        
    }
    //this takes you back to login
    //this is a test comment 
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
