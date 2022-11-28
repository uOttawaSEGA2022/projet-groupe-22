package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ComplaintsList extends ArrayAdapter<Complaint> {

    private Activity context;
    List<Complaint> complaints;

    public ComplaintsList(Activity context, List<Complaint> complaints) {
        super(context, R.layout.activity_complaints_list, complaints);
        this.context = context;
        this.complaints = complaints;
        System.out.println("**************************************in the complaints list class***********************");

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
        textViewDate.setText(complaint.getDate());
        textViewChef.setText(complaint.getChefUid());
        textViewComplaintText.setText(complaint.getComplaintText());

        return listViewItem;
    }
}