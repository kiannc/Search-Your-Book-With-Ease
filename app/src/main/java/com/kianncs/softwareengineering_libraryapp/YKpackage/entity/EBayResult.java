package com.kianncs.softwareengineering_libraryapp.YKpackage.entity;

import android.graphics.Bitmap;

/**
 * Created by chew on 19/10/2016.
 */
public class EBayResult implements Result{


    private String Title = "title";
    private String CurrentPrice  = "currentPrice";
    private String ViewItemURL = "viewItemURL";
    private Bitmap Image = null;

    public EBayResult(String currentPrice, String viewItemURL,String title, Bitmap image) {
        this.CurrentPrice = currentPrice;
        this.ViewItemURL = viewItemURL;
        this.Title = title;
        this.Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public String getCurrentPrice() {
        return "$"+CurrentPrice;
    }


    public String getViewItemURL() {
        return ViewItemURL;
    }

    public Bitmap getImage() {
        return Image;
    }


}

