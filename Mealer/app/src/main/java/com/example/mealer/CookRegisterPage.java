package com.example.mealer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CookRegisterPage extends AppCompatActivity {
    private Button donebutton;

    private Button submitchequebutton;

    private ImageView previewimage;

    int SELECT_PICTURE = 200;

    private EditText editTextname, editTextLastName, editTextinputEmail, editTextpassword, editTextcreditCard, editTextCVV, editTextExpiry;
    private EditText city, postalCode, phoneNumber, street;
    //private FirebaseAuth fAuth;
    private Address.Province province;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editTextname = (EditText) findViewById(R.id.inputname);
        editTextLastName = (EditText) findViewById(R.id.inputlastname);
        editTextinputEmail = (EditText) findViewById(R.id.inputemail);
        editTextpassword = (EditText) findViewById(R.id.inputpass);
        editTextcreditCard = (EditText) findViewById(R.id.creditcard);
        editTextCVV = (EditText) findViewById(R.id.cvv);
        editTextExpiry = (EditText) findViewById(R.id.expiry);
        city =findViewById(R.id.rcity);//add in xml
        postalCode=findViewById(R.id.rpostalCode);//add in xml
        phoneNumber=findViewById(R.id.rphoneNumber);//add in xml
        street=findViewById(R.id.rstreet);//add in xml
            /*once we add firebase
            fAuth = FirebaseAuth.getInstance();
        fDataRef = FirebaseDatabase.getInstance().getReference("UserData");
             */
        
        setContentView(R.layout.activity_cook_register_page);


        //setting done button's onclick
        donebutton = (Button) findViewById(R.id.donebutton);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        //initializing the submit button and image preview
        submitchequebutton = findViewById(R.id.submitchequebutton);
        previewimage = findViewById(R.id.previewimage);

        //setting onclick for the above
        submitchequebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });


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
        
        String inputPhoneNumber = phoneNumber.getText().toString().trim();
        final String inputCity = city.getText().toString().trim();
        final String inputStreet = street.getText().toString().trim();
        String inputPostalCode = postalCode.getText().toString().trim();
        final Address address;
        
            if(TextUtils.isEmpty(inputPhoneNumber)){phoneNumber.setError("Phone number is required. ");return;}
            if (inputPhoneNumber.length() == 10) {
                inputPhoneNumber = inputPhoneNumber.substring(0, 3)+ "-"
                        + inputPhoneNumber.substring(3, 6)+ "-"
                        + inputPhoneNumber.substring(6);
            }
            if (!Pattern.matches("^(\\d{3}-){2}\\d{4}$", inputPhoneNumber)) {
                phoneNumber.setError("Phone number is invalid. ex. 1234567891");
                return;
            }

            if(TextUtils.isEmpty(inputCity)){city.setError("City is Required. ");return;}
            if(TextUtils.isEmpty(inputStreet)){street.setError("Street is Required. ");return;}

            if(TextUtils.isEmpty(inputPostalCode)){postalCode.setError("Postal Code is Required. ");return;}

            if (inputPostalCode.length()==6){
                inputPostalCode = inputPostalCode.substring(0, 3)
                        + " "
                        + inputPostalCode.substring(3);
            }
            inputPostalCode=inputPostalCode.toUpperCase();

            Pattern phonePattern = Pattern.compile("[A-Z]\\d[A-Z] \\d[A-Z]\\d");
            Matcher m = phonePattern.matcher(inputPostalCode);
            if (!m.matches()) {
                postalCode.setError("Postal code is invalid. ex.m1a1d9");
                return;
            }

        final String employeePhoneNumber=inputPhoneNumber;
        final String employeePostalCode=inputPostalCode;
        
        /*
        fAuth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    String id = fAuth.getCurrentUser().getUid();
                        fDataRef.child(id).setValue(new EmployeeData(inputName, userRole, id, inputEmail, employeePhoneNumber, new Address (province, employeePostalCode, inputStreet, inputCity)));
                    
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
        });
         */

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


    //this takes you back to login
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
