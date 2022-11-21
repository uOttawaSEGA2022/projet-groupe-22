package com.example.mealer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;


public class CookRegisterPage extends AppCompatActivity {
    //TODO: FIX THIS
    Button donebutton;

    Button submitchequebutton;

    ImageView previewimage;

    int SELECT_PICTURE = 200;

    EditText inputName, inputLastName, inputEmail, inputPass, inputAddress, inputDescription;
    //TODO: find a way to store the image


    private FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_register_page);

        //hooks
        inputName = (EditText) findViewById(R.id.inputnamecook);
        inputLastName = (EditText) findViewById(R.id.inputlastnamecook);
        inputEmail = (EditText) findViewById(R.id.inputemailcook);
        inputPass = (EditText) findViewById(R.id.inputpasscook);
        inputAddress = (EditText) findViewById(R.id.addresscook);
        inputDescription = (EditText) findViewById(R.id.inputdescriptioncook);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        //setting done button's onclick
        donebutton = (Button) findViewById(R.id.donebuttoncook);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClicked();
            }
        }); //end of donebutton


        //initializing the submit button and image preview
        submitchequebutton = findViewById(R.id.submitchequebutton);
        previewimage = findViewById(R.id.previewimage);

        //setting onclick for the above
        submitchequebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        }); //end of chequebutton


    } //end of oncreate

    private void onRegisterButtonClicked(View view) {
        //Creating the getters for the inputs

        String name = inputName.getText().toString().trim();
        String lastname = inputLastName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPass.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();

        //Creating the error messages

        if (address.isEmpty()) {
            inputAddress.setError("Address is required");
            inputAddress.requestFocus();
            return;
        }

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

        if (password.isEmpty()) {
            inputPass.setError("Password is required");
            inputPass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            inputPass.setError("Email is required");
            inputPass.requestFocus();
            return;
        }

        if (description.isEmpty()){
            inputDescription.setError("Description is required!");
            inputDescription.requestFocus();
            return;
        }


        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {   //creating the user object
                            Cook cook = new Cook();
                            cook.setName(name);
                            cook.setLastName(lastname);
                            cook.setEmail(email);
                            cook.setPassword(password);
                            cook.setDescription(description);

                            //TODO: add other vals here

                            fUser = fAuth.getCurrentUser();
                            String IDstring = fUser.getUid();

                            reference.child("users").child(IDstring).setValue(cook);

                            Intent intent = new Intent(CookRegisterPage.this, MainActivity.class);
                            startActivity(intent);

                            Toast.makeText(CookRegisterPage.this, "Registration successful! Welcome", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(CookRegisterPage.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

        //this chooses the photo
    void imageChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if (data != null
                            && data.getData() != null){
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        previewimage.setImageBitmap(selectedImageBitmap);
                    }
                }
            });


}
