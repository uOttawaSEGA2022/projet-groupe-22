package com.example.mealer;

import androidx.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseAuth mAuth;

    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        listViewComplaints = (ListView) findViewById(R.id.listViewComplaints);
        complaints = new ArrayList<>();

        databaseComplaints = FirebaseDatabase.getInstance().getReference("complaints");

        //has been used to create the complaints

        String id = databaseComplaints.push().getKey();
        Complaint complaint = new Complaint(id, "WAt0uo7aplYqeY1ukVaCMKpyKLD3", "November", "I hate the food");


        //saving the product
        databaseComplaints.child(id).setValue(complaint);

        String id2 = databaseComplaints.push().getKey();
        Complaint complaint2 = new Complaint(id2, "WAt0uo7aplYqeY1ukVaCMKpyKLD3", "November", "I hate the foodjhgcdfgvfdvdyfgvdgv            ");




        //saving the product
        databaseComplaints.child(id2).setValue(complaint2);


        listViewComplaints.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                showUpdatedList(complaint.getComplaintId(), complaint.getChefUid());

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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(chefUid).child("name");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chefName = snapshot.getValue(String.class);
                dialogBuilder.setTitle("Complaint on " + chefName);

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
            }
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminPage.this, "Hey that didn't work out, sorry",Toast.LENGTH_LONG).show();
            }
        });

    }

    //dismissComplaint it just deletes the complaint from the list
    private boolean DismissComplaint(String id) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        System.out.println(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Complaint Dismissed", Toast.LENGTH_LONG).show();
        return true;
    }


    private boolean acceptAndGoToChefProfile(String id, String chefUid) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("complaints").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Complaint is deleted and now you will be directed to suspend user page", Toast.LENGTH_LONG).show();
        //TODO save the reference of the chef
        /*
        DatabaseReference Chefreference = FirebaseDatabase.getInstance().getReference().child("users").child(chefUid);
        Administrator.saveSuspChefRef(Chefreference);

         */
        Intent intent = new Intent(AdminPage.this, AdminSuspendUser.class);
        startActivity(intent);

        return true;
    }
    //TODO these methods should be in the adminSuspendUser activity
/*
private boolean suspendTemp (String chefID , String time){
        boolean result = false;
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth = FirebaseAuth.getInstance();
            String currentUserID = mAuth.getCurrentUser().getUid();
            if (currentUserID.equals(chefID)){
                //The status shouldn't be a boolean
                //It's better to use it like a string
                // Suspended def
                // Suspended Tem
                // Not suspended
                ref = FirebaseDatabase.getInstance().getReference().child("users").child(chefID).child("status");
                //TODO: Change the value in firebase
                //TODO: Add the timer for the suspension time
                // Change the statut after the suspension
                //TODO: Add the event listener
                Toast.makeText(getApplicationContext(), "Cook successfully suspended for !", Toast.LENGTH_LONG).show();
                result = true;
            }
        }
        return result;
    }

    private boolean suspendDef (String chefID){
        boolean result = false;
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth = FirebaseAuth.getInstance();
            String currentUserID = mAuth.getCurrentUser().getUid();
            if (currentUserID.equals(chefID)){
                ref = FirebaseDatabase.getInstance().getReference().child("users").child(chefID).child("status");
                //TODO: Change the value in firebase to suspended def
                Toast.makeText(getApplicationContext(), "Cook successfully suspended !", Toast.LENGTH_LONG).show();
                result = true;
            }
        }
        return result;
    }
 */


}
