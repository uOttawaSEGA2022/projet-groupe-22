package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//added implementation

public class ClientRegisterPage extends AppCompatActivity {

    EditText inputName, inputLastName, inputEmail, inputPass, inputAddress,
            creditCard, inputCVV, inputExpiry;
    Button donebutton;
    private FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register_page);

        //hooks to all xml elements

        inputName = (EditText) findViewById(R.id.inputname);
        inputLastName = (EditText) findViewById(R.id.inputlastname);
        inputEmail = (EditText) findViewById(R.id.inputemail);
        inputPass = (EditText) findViewById(R.id.inputpass);
        inputAddress = (EditText) findViewById(R.id.address);
        creditCard = (EditText) findViewById(R.id.creditcard);
        inputCVV = (EditText) findViewById(R.id.cvv);
        inputExpiry = (EditText) findViewById(R.id.expiry);
        donebutton = (Button) findViewById(R.id.donebuttonclient);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");

        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClicked();
                //TODO: add the other values
            }
        }); //donebutton method end


    } //oncreate end

    private void onRegisterButtonClicked() {
        //Creating the getters for the inputs

        String name = inputName.getText().toString().trim();
        String lastname = inputLastName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String pass = inputPass.getText().toString().trim();
        String creditcard = creditCard.getText().toString().trim();
        String cvv = inputCVV.getText().toString().trim();
        String expiry = inputExpiry.getText().toString().trim();

        //Creating the error messages

        if (name.isEmpty()) {
            inputName.setError("First name is required");
            inputName.requestFocus();
            return;
        }
        if (lastname.isEmpty()) {
            inputLastName.setError("Last name is required");
            inputLastName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please provide a valid email");
            inputEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            inputPass.setError("Password is required");
            inputPass.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            inputPass.setError("Email is required");
            inputPass.requestFocus();
            return;
        }

        if (creditcard.isEmpty()) {
            creditCard.setError("Credit card is required");
            creditCard.requestFocus();
            return;
        }
        if (cvv.isEmpty()) {
            inputCVV.setError("cvv is required");
            inputCVV.requestFocus();
            return;
        }
        if (cvv.length() < 3) {
            inputCVV.setError("The number should not be less than 3 numbers");
            inputCVV.requestFocus();
            return;
        }

        if (expiry.isEmpty()) {
            inputExpiry.setError("The expiry date is required");
            inputExpiry.requestFocus();
            return;
        }

        //TODO: allow for authentication here
        final String address = inputAddress.getText().toString();

        fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Client client = new Client();
                    client.setName(name);
                    client.setLastName(lastname);
                    client.setEmail(email);
                    client.setPassword(pass);
                    client.setAddress(address);


                    fUser = fAuth.getCurrentUser();
                    String IDstring = fUser.getUid();


                    reference.child("users").child(IDstring).setValue(client);

                    Intent intent = new Intent(ClientRegisterPage.this, MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(ClientRegisterPage.this,"Registration successful!",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(ClientRegisterPage.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
