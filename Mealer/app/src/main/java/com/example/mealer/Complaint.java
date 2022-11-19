package com.example.mealer;

public class Complaint {

    String clientUid;
    //TODO: should chef really be a string? it would make more sense as a reference
    String chef;
    String date;
    String complaintText="";

    //Constructors
    public Complaint (){}

    public Complaint(String clientUid, String chef, String date, String complaintText){
        this.clientUid=clientUid;
        this.chef=chef;
        this.date=date;
        this.complaintText=complaintText;
    }

    //Setters
    public void setClientUid(String clientUid){this.clientUid = clientUid;}
    public void setChef(String chef){ this.chef = chef;}
    public void setDate(String date){ this.date = date;}
    public void setComplaintText(String complaintText){this.complaintText = complaintText;}

    //getters
    public String getClientUid(){return this.clientUid;}
    public String getChef(){return this.chef;}
    public String getDate(){return this.date;}
    public String getComplaintText(){return this.complaintText;}
}
