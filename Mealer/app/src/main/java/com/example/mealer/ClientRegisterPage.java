package com.example.mealer;

package com.example.mealer_seg;

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

//added implememntation

public class ClientRegisterPage extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextname, editTextLastName, editTextinputEmail, editTextpassword, editTextcreditCard, editTextCVV, editTextExpiry;
    //private FirebaseAuth fAuth;

    private TextView creditText, donebutton;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register_page);

        donebutton = (Button) findViewById(R.id.donebutton);

        donebutton.setOnClickListener(this);

        editTextname = (EditText) findViewById(R.id.inputname);
        editTextLastName = (EditText) findViewById(R.id.inputlastname);
        editTextinputEmail = (EditText) findViewById(R.id.inputemail);
        editTextpassword = (EditText) findViewById(R.id.inputpass);
        editTextcreditCard = (EditText) findViewById(R.id.creditcard);
        editTextCVV = (EditText) findViewById(R.id.cvv);
        editTextExpiry = (EditText) findViewById(R.id.expiry);
            /*once we add firebase
            fAuth = FirebaseAuth.getInstance();
        fDataRef = FirebaseDatabase.getInstance().getReference("UserData");
             */
        }

    private void onRegisterButtonClicked(View view){
        //Creating the getters for the inputs

        String inputname=editTextname.getText().toString().trim();
        String inputlastname=editTextLastName.getText().toString().trim();
        String inputemail=editTextinputEmail.getText().toString().trim();
        String inputpass=editTextpassword.getText().toString().trim();
        String creditcard=editTextcreditCard.getText().toString().trim();
        String cvv=editTextCVV.getText().toString().trim();
        String expiry=editTextExpiry.getText().toString().trim();

        //Creating the error messages

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

        if (!Patterns.EMAIL_ADDRESS.matcher(inputemail).matches()){
            editTextinputEmail.setError("Please provide a valid email");
            editTextinputEmail.requestFocus();
            return;
        }

        if(inputpass.isEmpty()){
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            return;
        }

        if (inputpass.length()<6){
            editTextpassword.setError("Email is required");
            editTextpassword.requestFocus();
            return;
        }

        if(creditcard.isEmpty()){
            editTextcreditCard.setError("Credit card is required");
            editTextcreditCard.requestFocus();
            return;
        }
        if(cvv.isEmpty()){
            editTextCVV.setError("cvv is required");
            editTextCVV.requestFocus();
            return;
        }
        if (cvv.length()<3){
            editTextCVV.setError("The number should not be less than 3 numbers");
            editTextCVV.requestFocus();
            return;
        }

        if(expiry.isEmpty()){
            editTextExpiry.setError("The expiry date is required");
            editTextExpiry.requestFocus();
            return;
        }

    }
    /*
    public void onComplete(@NonNull Task<AuthResult> task){
                if (task.isSuccessful())
                {
                    String id = fAuth.getCurrentUser().getUid();
                        fDataRef.child(id).setValue(new UserData(inputName, userRole, id, inputEmail));
                    Toast.makeText(Register.this,"User Created",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish();
                    }
                else
                {
                    Toast.makeText(Register.this,"Error! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
    }
     */

    //this takes you back to login
    //this is a test comment
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
}
