package com.kianncs.softwareengineering_libraryapp.YKpackage.API;

import com.kianncs.softwareengineering_libraryapp.APIAndDatabaseFactory;
import com.kianncs.softwareengineering_libraryapp.YKpackage.entity.Result;

import java.util.ArrayList;

/**
 * Created by Yq on 4/11/2016.
 */

public class APIManager {
    private API api;
    public APIManager(String ApiType){
        api = APIAndDatabaseFactory.getAPI(ApiType);
    }

    public ArrayList<Result> getApiSearchResult(String keyword, String pageNumber){
        return api.getApiSearchResult(keyword,pageNumber);
    }
}
