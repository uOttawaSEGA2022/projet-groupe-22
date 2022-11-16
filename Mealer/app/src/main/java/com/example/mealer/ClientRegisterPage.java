package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//added implementation

public class ClientRegisterPage extends AppCompatActivity {

    EditText inputName, inputLastName, inputEmail, inputPass, inputAddress,
            creditCard, inputCVV, inputExpiry;
    Button donebutton;

    FirebaseDatabase rootNode;
    DatabaseReference reference;



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
        donebutton = (Button) findViewById(R.id.donebutton);

        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                //get all vals
                String name = inputName.getText().toString();
                String lastname = inputLastName.getText().toString();
                String email = inputEmail.getText().toString();
                String lastname = inputLastName.getText().toString();
                String lastname = inputLastName.getText().toString();
                String lastname = inputLastName.getText().toString();
                String lastname = inputLastName.getText().toString();
                String lastname = inputLastName.getText().toString();

                Client client = new Client();

                reference.setValue("we storing data boys");
            }
        }); //donebutton method end

    } //oncreate end

    private void onRegisterButtonClicked(View view) {
        //Creating the getters for the inputs

        String inputname = editTextname.getText().toString().trim();
        String inputlastname = editTextLastName.getText().toString().trim();
        String inputemail = editTextinputEmail.getText().toString().trim();
        String inputpass = editTextpassword.getText().toString().trim();
        String creditcard = editTextcreditCard.getText().toString().trim();
        String cvv = editTextCVV.getText().toString().trim();
        String expiry = editTextExpiry.getText().toString().trim();

        //Creating the error messages

        if (inputname.isEmpty()) {
            editTextname.setError("First name is required");
            editTextname.requestFocus();
            return;
        }
        if (inputlastname.isEmpty()) {
            editTextLastName.setError("Last name is required");
            editTextLastName.requestFocus();
            return;
        }
        if (inputemail.isEmpty()) {
            editTextinputEmail.setError("Email is required");
            editTextinputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputemail).matches()) {
            editTextinputEmail.setError("Please provide a valid email");
            editTextinputEmail.requestFocus();
            return;
        }

        if (inputpass.isEmpty()) {
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            return;
        }

        if (inputpass.length() < 6) {
            editTextpassword.setError("Email is required");
            editTextpassword.requestFocus();
            return;
        }

        if (creditcard.isEmpty()) {
            editTextcreditCard.setError("Credit card is required");
            editTextcreditCard.requestFocus();
            return;
        }
        if (cvv.isEmpty()) {
            editTextCVV.setError("cvv is required");
            editTextCVV.requestFocus();
            return;
        }
        if (cvv.length() < 3) {
            editTextCVV.setError("The number should not be less than 3 numbers");
            editTextCVV.requestFocus();
            return;
        }

        if (expiry.isEmpty()) {
            editTextExpiry.setError("The expiry date is required");
            editTextExpiry.requestFocus();
            return;
        }


        final Address address;


        fAuth.createUserWithEmailAndPassword(inputemail, inputpass)
                        .

                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {   //creating the user object
                            User user = new User(inputemail, inputpass);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                        public void onComplete(Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ClientRegisterPage.this, "User has been successful", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(ClientRegisterPage.this, "User has not been successful. Try again!", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                        } else {
                            Toast.makeText(ClientRegisterPage.this, "User has not been successful. Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    //this takes you back to login
    //this is a test comment
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
