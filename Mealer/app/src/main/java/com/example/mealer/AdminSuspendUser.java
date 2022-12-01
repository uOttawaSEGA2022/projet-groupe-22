package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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


                //chefReference = Administrator.getSuspChefRef();
            }
        });
    }
//TODO take the chef id from the admin class

    private void suspendTemp (String chefID , String time){
            //chefID.setInitialtime(time);
    }

    private void suspendDef (String chefID){
            //chefID.changeStatus();
    }
}
