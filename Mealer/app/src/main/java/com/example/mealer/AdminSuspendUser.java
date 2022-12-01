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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                suspDate.setText(date);

            }
        });

        setSuspDateBtn = (Button) findViewById(R.id.setSuspDateBtn);
        setSuspDateBtn.setOnClickListener(new View.OnClickListener() {

            //TODO the method should takes the value of the chosen date
            @Override
            public void onClick(View v) {suspendTemp(12453L);}
        });

        SuspEterBtn = (Button) findViewById(R.id.SuspEterBtn);
        SuspEterBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {suspendDef();}
        });

    }

    private void suspendTemp ( Long time){
        updateProfile(chefID, 1, time);
    }

    private void suspendDef (){
        updateProfile(chefID, 2, 0L);
    }

    private void updateProfile(String id, int status, long susTime){
        //getting the specified chef reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(id);

        //updating profile
        dR.child("status").setValue(status);
        dR.child("susTime").setValue(susTime);

        Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_LONG).show();

    }
}
