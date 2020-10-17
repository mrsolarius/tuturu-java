package net.proximastro.app;

import java.util.ArrayList;
import java.util.HashMap;

public class RouteController {
    private HashMap<String,String> reqParams;
    private HashMap<String,String> urlParam;
    private String URI;

    public RouteController(){

    }

    public void render(String URI, HashMap<String,String> reqParams, HashMap<String,String> urlParam) {
        this.URI = URI;
        this.reqParams = reqParams;
        this.urlParam = urlParam;
    }
}
