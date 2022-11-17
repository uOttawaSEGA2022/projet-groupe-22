package com.example.mealer;

package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button loginbutton;
    private Button registerbutton;
    private EditText inputPassword, inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void openPageMain(){
        Intent intent = new Intent(this,PageMain.class);
        startActivity(intent);
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
        }
        openPageMain(); //sends to main page
    }

}

