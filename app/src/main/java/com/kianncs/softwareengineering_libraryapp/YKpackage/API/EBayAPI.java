package com.kianncs.softwareengineering_libraryapp.YKpackage.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kianncs.softwareengineering_libraryapp.YKpackage.entity.EBayResult;
import com.kianncs.softwareengineering_libraryapp.YKpackage.entity.Result;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created by chew on 19/10/2016.
 */
public class EBayAPI extends API{

    private Bitmap bmp;
    private String currentPrice  = "currentPrice";
    private String viewItemURL = "viewItemURL";
    private String conditionDisplayName = "conditionDisplayName";
    private String title = "title";
    private String galleryURL = "galleryURL";
    private String EbayUrl = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced";
    private String ServiceVersion="&SERVICE-VERSION=1.0.0&GLOBAL-ID=EBAY-SG";
    private String EbayID = "&SECURITY-APPNAME=yongqint-liakbook-PRD-2bff3fe36-1040ea59";
    private String ResponseType="&RESPONSE-DATA-FORMAT=XML&REST-PAYLOAD";
    private String Currency = "&currencyId=SGD";
    private String Category = "&categoryId=279";
    private String Response_10 = "&paginationInput.entriesPerPage=10";
    private String Page = "&paginationInput.pageNumber=";
    private String urlString = null;

    private XmlPullParserFactory xmlFactoryObject;

    public volatile boolean parsingComplete = true;

    public URL getURL(String keyword,String pageNumber){

        keyword = keywordEncoder(keyword);

        urlString= EbayUrl+ServiceVersion+EbayID+ResponseType+Category+Currency+Response_10+"&keywords="+keyword+Page+pageNumber;

        URL url = null;
        try {
            Log.i("API STRING", urlString);
            url = new URL(urlString);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    public ArrayList<Result> getApiSearchResult(String keyword,String pageNumber) {

        ArrayList<Result> ALResult = new ArrayList();


        int event;
        String text = null;
        String XMLSTORE = "asdoiwoiefjweijfoiwjfiowejofwi";
        Log.i("xmlstoreit", XMLSTORE);
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
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("currentPrice")) {
                            currentPrice = text;
                        } else if (name.equals("viewItemURL")) {
                            viewItemURL = text;
                        } else if (name.equals("title")) {
                            title = text;
                        } else if (name.equals("galleryURL")) {
                            galleryURL = text;
                            URL url = null;

                            try {
                                url = new URL(galleryURL);
                                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {e.printStackTrace();

                            }
                        } else if (name.equals("conditionDisplayName")) {
                            conditionDisplayName = text;
                        } else {
                            if (name.equals("item")) {
                                EBayResult result = new EBayResult(currentPrice, viewItemURL, title,bmp);
                                ALResult.add(result);
                            }
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ALResult;
    }

    private String keywordEncoder(String s) {
        String out;
        try {
            out = URLEncoder.encode(s, "UTF-8")
                    .replace(" ", "%20");
        } catch (UnsupportedEncodingException e) {
            out = s;
        }
        return out;
    }
}
