package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PageMain extends AppCompatActivity {
    FirebaseUser fUser;
    FirebaseAuth fAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    Button addComplaintBtn;
     Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_main);

        addComplaintBtn = findViewById(R.id.addComplaintBtn);
        addComplaintBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {addComplaint();}
        });
        
        signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fAuth.signOut();
                finish();
                Intent intent = new Intent(PageMain.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addComplaint(){

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.add_complaint, null);
            dialogBuilder.setView(dialogView);

            final EditText ComplaintChefName = (EditText) dialogView.findViewById(R.id.ComplaintChefName);
            final EditText complaintDate  = (EditText) dialogView.findViewById(R.id.complaintDate);
            final EditText complaintText  = (EditText) dialogView.findViewById(R.id.complaintText);
            final Button addComplaintBtn = (Button) dialogView.findViewById(R.id.addComplaintBtn);

            dialogBuilder.setTitle("Add complaint");
            final AlertDialog b = dialogBuilder.create();
            b.show();

                addComplaintBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {{

                    String chefName = ComplaintChefName.getText().toString().trim();
                    String date = complaintDate.getText().toString().trim();
                    String text = complaintText.getText().toString().trim();

                    DatabaseReference complaintReference = FirebaseDatabase.getInstance().getReference().child("complaints");

                    //checking if the value is provided
                    if (!(TextUtils.isEmpty(chefName) & TextUtils.isEmpty(date) & TextUtils.isEmpty(text))) {

                        //getting a unique id using push().getKey() method
                        //it will create a unique id and we will use it as the Primary key for our Complaint
                        String id = reference.push().getKey();

                        //creating a Product Object
                        Complaint complaint = new Complaint(id, chefName, date, text);

                        //saving the product
                        complaintReference.child(id).setValue(complaint);

                        //setting edittext to blank again
                        ComplaintChefName.setText("");
                        complaintDate.setText("");
                        complaintText.setText("");

                        //displaying a success toast
                        successToaster();
                        b.dismiss();
                    } else{
                        //if the values are not given displaying a toast
                        failingToaster();
                        b.dismiss();

                    }
                }}
            });
    }

    public void successToaster(){Toast.makeText(this, "Complaint added", Toast.LENGTH_LONG).show();}
    public void failingToaster(){Toast.makeText(this, "Try again and fill all the fields with the needed info", Toast.LENGTH_LONG).show();}


    public void onStart() {
        super.onStart();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        String IDstring = fUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(IDstring).child("name");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                Toast.makeText(PageMain.this,"Welcome, "+ name +"!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
