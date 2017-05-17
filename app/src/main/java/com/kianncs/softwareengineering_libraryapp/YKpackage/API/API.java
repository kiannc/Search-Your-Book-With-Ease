package com.kianncs.softwareengineering_libraryapp.YKpackage.API;

/**
 * Created by chew on 18/10/2016.
 */

import android.util.Log;

import com.kianncs.softwareengineering_libraryapp.YKpackage.entity.Result;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public abstract class API {

    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;


    public abstract URL getURL(String keyword,String pageNumber);
    public abstract ArrayList<Result> getApiSearchResult(String keyword,String pageNumber);


    public InputStream fetchStream(final URL url) throws XmlPullParserException {

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream stream = conn.getInputStream();
            Log.i("AFTER STREAM", stream.toString());

            return stream;

        } catch (Exception e) {
            return null;
        }




    }



}
