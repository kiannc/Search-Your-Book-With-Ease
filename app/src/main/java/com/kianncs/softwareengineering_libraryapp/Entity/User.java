package com.kianncs.softwareengineering_libraryapp.Entity;

/**
 * Created by kiann on 26/10/2016.
 */

public class User {
    private String name, email,  mobileNo;

    public User(){

    }

    public User(String name, String email,String mobileNo){
        this.name = name;
        this.email = email;
        this.mobileNo = mobileNo;
    }

    public String getName(){return name;}

    public String getEmail(){return email;}

    public String getMobileNo(){return mobileNo;}
}
