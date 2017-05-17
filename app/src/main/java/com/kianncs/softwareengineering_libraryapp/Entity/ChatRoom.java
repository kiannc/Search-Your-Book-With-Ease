package com.kianncs.softwareengineering_libraryapp.Entity;

/**
 * Created by chew on 31/10/2016.
 */

public class ChatRoom {

    private String userID,userName;

    public ChatRoom(){

    }

    public ChatRoom(String userID,String userName)
    {
        this.userID = userID;
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }
}
