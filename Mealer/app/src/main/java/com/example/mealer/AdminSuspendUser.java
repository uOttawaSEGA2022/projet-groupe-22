package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminSuspendUser extends AppCompatActivity {

    DatabaseReference  chefReference;
    Boolean status;

    FirebaseAuth fAuth;
    FirebaseUser fUser;

    CalendarView calendarView;
    TextView suspDate;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String date;
    Button setSuspDateBtn;
    Button SuspEterBtn;
    String chefID = AdminPage.getValue();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_suspend_user);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        suspDate = (TextView) findViewById(R.id.suspDate);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                //Date format : MM/DD/YYYY
                date = (i1 + 1) + "/" + i2 + "/" + i;
                suspDate.setText(date);

            }
        });

        setSuspDateBtn = (Button) findViewById(R.id.setSuspDateBtn);
        setSuspDateBtn.setOnClickListener(new View.OnClickListener() {

            //TODO the method should takes the value of the chosen date
            @Override
            public void onClick(View v) {suspendTemp(date);}
        });

        SuspEterBtn = (Button) findViewById(R.id.SuspEterBtn);
        SuspEterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {suspendDef();}
        });

    }

    private void suspendTemp ( String date){updateProfile(chefID, 1, date);}

    private void suspendDef (){
        updateProfile(chefID, 2, null);
    }

    private void updateProfile(String id, int status, String susDate){
        //getting the specified chef reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(id);

        //updating profile
        dR.child("status").setValue(status);
        dR.child("susTime").setValue(susDate);

        DatabaseReference mealsReference = FirebaseDatabase.getInstance().getReference("meals").child(id);
        mealsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //listening through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Meal meal = postSnapshot.getValue(Meal.class);
                    meal.setDisplay(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_LONG).show();

    }
}
