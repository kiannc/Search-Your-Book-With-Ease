package com.kianncs.softwareengineering_libraryapp.YKpackage;


import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chew on 18/10/2016.
 */
public class XMLParser {

    private String ASIN = "ASIN";
    private String DetailPageURL = "DetailPageURL";
    private String ItemAttributes = "ItemAttributes";
    private String Author = "Author";
    private String Title = "Title";

    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public XMLParser(String url){
        this.urlString = url;
    }

    public String getASIN(){
        return ASIN;
    }

    public String getDetailPageUrl(){
        return DetailPageURL;
    }

    public String getAuthor(){
        return Author;
    }

    public String getTitle(){
        return Title;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;
        String XMLSTORE = "asdoiwoiefjweijfoiwjfiowejofwi";
        Log.i("xmlstoreit",XMLSTORE);
        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();
                //Log.i("while loop name",name);
                switch (event){
                    case XmlPullParser.START_TAG:

                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        Log.i("XML parser",text);
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("ASIN")){

                            ASIN = text;
                        }
                        else if(name.equals("DetailPageURL"))
                        {
                            DetailPageURL = text;
                        }

                        else if(name.equals("Author")){
                            Author = text;
                        }

                        else if(name.equals("Title")){
                            Title = text;
                        }

                        else{
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        //
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try
                {
                    URL url = new URL(urlString);
                    Log.i("AFTER URL",url.toString());
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);
                    Log.i("AFTER STREAM",stream.toString());
                    parseXMLAndStoreIt(myparser);
                    stream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }



}
