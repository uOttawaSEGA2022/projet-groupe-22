package com.example.mealer;

public class Complaint {

    String complaintId;
    String chefUid;
    String date;
    String complaintText="";

    //Constructors
    public Complaint (){}

    public Complaint(String complaintid, String chefUid, String date, String complaintText){
        this.complaintId = complaintid;
        this.chefUid=chefUid;
        this.date=date;
        this.complaintText=complaintText;
    }

    //Setters
    public void setComplaintId(String complaintId){this.complaintId = complaintId;}
    public void setChefUid(String chef){ this.chefUid = chef;}
    public void setDate(String date){ this.date = date;}
    public void setComplaintText(String complaintText){this.complaintText = complaintText;}

    //getters
    public String getComplaintId(){return this.complaintId;}
    public String getChefUid(){return this.chefUid;}
    public String getDate(){return this.date;}
    public String getComplaintText(){return this.complaintText;}
}
