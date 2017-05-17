package com.kianncs.softwareengineering_libraryapp.Entity;

/**
 * Created by kiann on 19/10/2016.
 */

public class BookDataProvider {


    private String bookTitle;
    private String bookPrice;

    public BookDataProvider(String bookTitle, String bookPrice){
        this.setBookTitle(bookTitle);
        this.setBookPrice(bookPrice);
    }


    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }
}

