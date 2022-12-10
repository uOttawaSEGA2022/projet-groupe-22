package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ComplaintsList extends ArrayAdapter<Complaint> {

    private Activity context;
    List<Complaint> complaints;

    DatabaseReference complaintReference;

    public ComplaintsList(Activity context, List<Complaint> complaints) {
        super(context, R.layout.activity_complaints_list, complaints);
        this.context = context;
        this.complaints = complaints;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_complaints_list, null, true);

        //TODO : set the date format
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);
        TextView textViewChef = (TextView) listViewItem.findViewById(R.id.textViewChef);
        TextView textViewComplaintText = (TextView) listViewItem.findViewById(R.id.textViewComplaintText);

        Complaint complaint = complaints.get(position);
        textViewDate.setText("The complaint set date : "+complaint.getDate());

        complaintReference = FirebaseDatabase.getInstance().getReference().child("users").child(complaint.chefUid).child("name");
        complaintReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chefName = snapshot.getValue(String.class).toString();
                //setChefName(chefName);
                textViewChef.setText("Chef name : "+chefName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //textViewChef.setText("Chef name : "+complaint.getChefName());
        textViewComplaintText.setText("The complaint text : "+complaint.getComplaintText());

        return listViewItem;
    }
}
