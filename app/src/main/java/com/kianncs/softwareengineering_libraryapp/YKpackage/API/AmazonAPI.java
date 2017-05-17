package com.kianncs.softwareengineering_libraryapp.YKpackage.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import com.kianncs.softwareengineering_libraryapp.YKpackage.SignedRequestsHelper;
import com.kianncs.softwareengineering_libraryapp.YKpackage.entity.AmazonResult;
import com.kianncs.softwareengineering_libraryapp.YKpackage.entity.Result;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by chew on 18/10/2016.
 */
public class AmazonAPI extends API {

    private Bitmap bmp;
    private String DetailPageURL = "DetailPageURL";
    private String Title = "Title";
    private String ImageUrl = "ImageUrl";
    private String FormattedPrice = "FormattedPrice";
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public URL getURL(String keyword,String pageNumber) {
        try {
            SignedRequestsHelper SRHurl = new SignedRequestsHelper();
            // map to amazon sign class using map<string,string>
            Map<String, String> params = new HashMap<String, String>();
            params.put("Version", "2011-08-01");
            params.put("AssociateTag", "ysokang-20");
            params.put("Operation", "ItemSearch");
            params.put("SearchIndex", "Books");
            params.put("ItemPage",pageNumber);
            params.put("ResponseGroup","ItemAttributes,Images,Offers");
            params.put("Keywords", keyword);

            String AAA = SRHurl.sign(params);
            Log.i("API STRING", AAA);
            URL url = new URL(AAA);
            return url;
        }catch(Exception e){

        }
        return null;
    }
    public ArrayList<Result> getApiSearchResult(String keyword,String pageNumber) {

        //retrieve specific data set from XML response
        ArrayList<Result> ALResult = new ArrayList();
        boolean ListPrice = false, Image = false;
        int event;
        String text=null;
        // using Log to check for error not important
        String XMLSTORE = "asdoiwoiefjweijfoiwjfiowejofwi";
        Log.i("xmlstoreit",XMLSTORE);

        XmlPullParser myParser;
        InputStream stream;

        try {
            stream = fetchStream(getURL(keyword,pageNumber));
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            myParser = xmlFactoryObject.newPullParser();
            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(stream, null);

            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();
                //Log.i("while loop name",name);
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(name.equals("ListPrice")) {
                            ListPrice = true;
                        }else if(name.equals("SmallImage")){
                            Image = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        //Log.i("XML parser",text);
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("DetailPageURL")) {
                            DetailPageURL = text;
                        }else if(name.equals("Title")){
                            Title = text;
                        }else if(name.equals("FormattedPrice")&&ListPrice == true){
                            FormattedPrice = text;
                            ListPrice = false;
                        }else if(name.equals("URL")&&Image == true){
                            URL url = null;

                            ImageUrl = text;
                            Image = false;
                            try {
                                url = new URL(ImageUrl);
                                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {e.printStackTrace();

                            }
                        }else{
                            if(name.equals("Item")){
                                // when xml data reach the end of 1 item store in obj and add to arraylist
                                AmazonResult result = new AmazonResult(bmp,Title,FormattedPrice,DetailPageURL);
                                ALResult.add(result);
                            }
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
            stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ALResult;
    }





}
