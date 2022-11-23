package com.example.mealer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminPage extends AppCompatActivity {

    ListView listViewComplaints;

    List<Complaint> complaints;

    DatabaseReference databaseComplaints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        listViewComplaints = (ListView) findViewById(R.id.listViewComplaints);
        complaints = new ArrayList<>();

        databaseComplaints = FirebaseDatabase.getInstance().getReference("complaints");

        //has been used to create the complaints

        /*String id = databaseComplaints.push().getKey();
        Complaint complaint = new Complaint("first", "WAt0uo7aplYqeY1ukVaCMKpyKLD3", "November", "I hate the food");


        //saving the product
        databaseComplaints.child(id).setValue(complaint);

        String id2 = databaseComplaints.push().getKey();
        Complaint complaint2 = new Complaint("second", "WAt0uo7aplYqeY1ukVaCMKpyKLD3", "November", "I hate the food");




        //saving the product
        databaseComplaints.child(id2).setValue(complaint2);
*/

        listViewComplaints.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                showUpdatedList(complaint.getId(), complaint.getChefUid());

                return true;
            }
        });

    }

@Override
    protected void onStart(){
        super.onStart();


        databaseComplaints.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                //clearing the previous artist list
                complaints.clear();

                //listening through all the nodes
                //for(DataSnapshot postSnapshot : dataSnapshot.child("complaints").getChildren()){
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    //getting products

                    Complaint complaint = postSnapshot.getValue(Complaint.class);
                    //adding product to the list
                    complaints.add(complaint);
                }
                //creating adapter
                ComplaintsList complaintsAdapter = new ComplaintsList(AdminPage.this, complaints);
                //attaching adapter to the listview
                listViewComplaints.setAdapter(complaintsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }

        });

    }




    //it should be called after clicking the buttons
    //it has the button listeners
    private void showUpdatedList(final String complaintId, String chefUid){

        //Set the box that will show after the long Clicking on the Complaint
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.updatedcomplaintslist, null);
        dialogBuilder.setView(dialogView);

        final Button buttonAcceptComplaint = (Button) dialogView.findViewById(R.id.buttonAcceptComplaint);
        final Button buttonDismissComplaint = (Button) dialogView.findViewById(R.id.buttonDismissComplaint);
        final Button buttonGoBack = (Button) dialogView.findViewById(R.id.buttonGoBack);

        dialogBuilder.setTitle("Complaint on " + chefUid);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAcceptComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptAndGoToChefProfile(complaintId, chefUid);
                b.dismiss();
            }
        });

        buttonDismissComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DismissComplaint(complaintId);
                b.dismiss();
            }
        });

        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

    }

    //dismissComplaint it just deletes the complaint from the list
    private boolean DismissComplaint(String id) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Complaint Dismissed", Toast.LENGTH_LONG).show();
        return true;
    }


    private boolean acceptAndGoToChefProfile(String id, String chefUid) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Complaint is deleted and you will be directed to the chef's profile", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(AdminPage.this, CookPage.class);
        startActivity(intent);

        return true;
    }

}
