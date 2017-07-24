package com.example.btril.friend_stalker;

/**
 * Created by juhi on 7/24/17.
 */

public class Friend {

    private String fn; //first name
    private String ln;
    private String email;
    private long Phonenumber;
    private double distance;
    private double latitude;
    private double longitude;



    public Friend(){

    }
    public Friend(String fn,String ln,String email,long number,double distance){
        this.fn=fn;
        this.ln=ln;
        this.email=email;
        this.Phonenumber=number;
        this.distance=distance;


    }

    public String getfn() { return fn;}
    public void setfn(String fn) { this.fn=fn;}

    public String getLn() { return ln;}
    public void setLn(String ln) { this.ln=ln;}

    public String getEmail() { return email;}
    public void setEmail(String email) { this.email=email;}

    public long getNumber() { return Phonenumber;}
    public void setNumber(long number) { this.Phonenumber=number;}

    public double getdistance() { return distance;}
    public void setDistance(double distance) { this.distance=distance;}

    public double getlongitude() { return longitude;}

    public void setLongitude(double longitude) { this.longitude=longitude;}


    public double getlatitude() { return latitude;}
    public void setLatitude(double latitude) { this.latitude=latitude;}
}

