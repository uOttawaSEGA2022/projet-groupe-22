package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button loginbutton;
    private Button registerbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginbutton = (Button) findViewById(R.id.loginbutton); //initialize
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPageMain(); //sends to main page
            }
        });

        registerbutton = (Button) findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            //  TODO: edit this onClick to only happen upon verification of login
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
}

