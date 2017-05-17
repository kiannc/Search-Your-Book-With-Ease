package com.kianncs.softwareengineering_libraryapp.Entity;

/**
 * Created by kiann on 26/10/2016.
 */

public class Comment {

    public String userID;
    public String userName;
    public String text;

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public Comment() {
    }

    public Comment(String userID, String userName, String text) {
        this.userID = userID;
        this.userName = userName;
        this.text = text;
    }

}
