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
    private FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        setContentView(R.layout.activity_client_register_page);

        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                //get all vals
                String name = inputName.getText().toString();
                String lastname = inputLastName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPass.getText().toString();
                //TODO: add the other values

                Client client = new Client();
                client.setName(name);
                client.setLastName(lastname);
                client.setEmail(email);
                client.setPassword(password);

                reference.child(name).setValue(client);
            }
        }); //donebutton method end


    } //oncreate end

    private void onRegisterButtonClicked(View view) {
        //Creating the getters for the inputs

        String inputname = inputName.getText().toString().trim();
        String inputlastname = inputLastName.getText().toString().trim();
        String inputemail = inputEmail.getText().toString().trim();
        String inputpass = inputPass.getText().toString().trim();
        String creditcard = creditCard.getText().toString().trim();
        String cvv = inputCVV.getText().toString().trim();
        String expiry = inputExpiry.getText().toString().trim();

        //Creating the error messages

        if (inputname.isEmpty()) {
            inputName.setError("First name is required");
            inputName.requestFocus();
            return;
        }
        if (inputlastname.isEmpty()) {
            inputLastName.setError("Last name is required");
            inputLastName.requestFocus();
            return;
        }
        if (inputemail.isEmpty()) {
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputemail).matches()) {
            inputEmail.setError("Please provide a valid email");
            inputEmail.requestFocus();
            return;
        }

        if (inputpass.isEmpty()) {
            inputPass.setError("Password is required");
            inputPass.requestFocus();
            return;
        }

        if (inputpass.length() < 6) {
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
