package com.example.mealer;

public class Complaint {

    String clientUid;
    //TODO: should chef really be a string? it would make more sense as a reference
    String chefUid;
    String date;
    String complaintText="";

    //Constructors
    public Complaint (){}

    public Complaint(String clientUid, String chefUid, String date, String complaintText){
        this.clientUid=clientUid;
        this.chefUid=chefUid;
        this.date=date;
        this.complaintText=complaintText;
    }

    //Setters
    public void setClientUid(String clientUid){this.clientUid = clientUid;}
    public void setChefUid(String chef){ this.chefUid = chef;}
    public void setDate(String date){ this.date = date;}
    public void setComplaintText(String complaintText){this.complaintText = complaintText;}

    //getters
    public String getClientUid(){return this.clientUid;}
    public String getChefUid(){return this.chefUid;}
    public String getDate(){return this.date;}
    public String getComplaintText(){return this.complaintText;}
}
