package com.kianncs.softwareengineering_libraryapp.Entity;

/**
 * Created by kiann on 26/10/2016.
 */

public class Post {

    private String userID,userName,bookTitle,bookAuthor,bookPrice,bookCondition;

    public Post(){

    }

    public Post(String userID, String userName, String bookTitle, String bookAuthor, String bookPrice,
                String bookCondition) {
        this.userID = userID;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.bookCondition = bookCondition;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName(){ return userName; }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public String getBookCondition() {
        return bookCondition;
    }
}
