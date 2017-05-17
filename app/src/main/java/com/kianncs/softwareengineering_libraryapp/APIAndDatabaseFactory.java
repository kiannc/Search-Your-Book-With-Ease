package com.kianncs.softwareengineering_libraryapp;

import com.kianncs.softwareengineering_libraryapp.YKpackage.API.API;
import com.kianncs.softwareengineering_libraryapp.YKpackage.API.AmazonAPI;
import com.kianncs.softwareengineering_libraryapp.YKpackage.API.EBayAPI;

/**
 * Created by Yq on 4/11/2016.
 */

public class APIAndDatabaseFactory {
    private static iDatabase firebaseDBImp = null;

    public static iDatabase getDatabase(String databaseType){
        iDatabase database = null;
        if(databaseType.equals("firebase")) {
            if (firebaseDBImp == null)
                firebaseDBImp = new FirebaseDBImp();
            database = firebaseDBImp;
        }
        return database;
    }

    public static API getAPI(String ApiType){
        API api = null;
        if(ApiType.equals("amazon"))
            api = new AmazonAPI();
        else if(ApiType.equals("ebay"))
            api = new EBayAPI();
        return api;
    }
}
