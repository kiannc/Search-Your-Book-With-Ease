package com.kianncs.softwareengineering_libraryapp.YKpackage.entity;

import android.graphics.Bitmap;

/**
 * Created by chew on 18/10/2016.
 */
public class AmazonResult implements Result {


    private String Title = "Title";
    private String CurrentPrice = "CurrentPrice";
    private String ViewItemURL = "ViewItemURL";
    private Bitmap Image = null;

    public AmazonResult(Bitmap image, String title, String currentPrice, String viewItemURL) {
        Image = image;
        Title = title;
        CurrentPrice = currentPrice;
        ViewItemURL = viewItemURL;
    }

    public String getTitle() {
        return Title;
    }

    public String getCurrentPrice(){
        return CurrentPrice;
    }

    public String getViewItemURL(){
        return ViewItemURL;
    }

    public Bitmap getImage(){
        return Image;
    }


}

