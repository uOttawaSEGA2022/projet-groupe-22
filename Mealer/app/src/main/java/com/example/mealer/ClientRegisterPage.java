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
        
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
        
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
    //this takes you back to login
    //this is a test comment 
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
