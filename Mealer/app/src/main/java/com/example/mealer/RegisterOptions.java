package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterOptions extends AppCompatActivity {
    private Button asclientbutton;
    private Button ascookbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_options);

        asclientbutton = (Button) findViewById(R.id.asclientbutton);
        asclientbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClientRegisterPage();
            }
        });

        ascookbutton = (Button) findViewById(R.id.ascookbutton);
        ascookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCookRegisterPage();
            }
        });
    }

    public void openClientRegisterPage(){
        Intent intent = new Intent(this, ClientRegisterPage.class);
        startActivity(intent);
    }
    public void openCookRegisterPage(){
        Intent intent = new Intent(this, CookRegisterPage.class);
        startActivity(intent);
    }
}