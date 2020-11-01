package net.proximastro.app;

import java.util.HashMap;
import java.util.List;

public class RouteController {
    protected HashMap<String,String> GETMap;
    protected HashMap<String,String> URIMap;
    protected HashMap<String,String> POSTMap;
    protected List<String> handleMethods;
    protected String URI;
    protected String routeURI;

    public RouteController(){}

    public void setHandleMethods(List<String> handleMethods) {
        if (this.handleMethods==null){
            this.handleMethods = handleMethods;
        }
    }

    public List<String> getHandleMethods() {
        return handleMethods;
    }

    protected String index(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "   <meta charset=\"UTF-8\">\n" +
                "   <title>Page par defaut</title>\n" +
                "   <link rel=\"stylesheet\" href=\"/css/bootstrap.min.css\">" +
                "   <script src=\"/js/bootstrap.min.js\"></script>" +
                "</head>\n" +
                "<body>" +
                "   <h1>Données</h1>" +
                "   <h3>URI</h2>" +
                "   <p>Route URI : <code>"+routeURI+"</code></p>" +
                "   <p>Local URI : <code>"+URI+"</code></p>" +
                "   <h3>Get Parameters</h2>" +
                "   <p>HashMap GET : <code>"+GETMap.toString()+"</code></p>" +
                "   <p>HashMap URI : <code>"+URIMap.toString()+"</code></p>" +
                "   <p>HashMap POST: <code>"+POSTMap.toString()+"</code></p>" +
                "</body>" +
                "</html>";
    }

    String render(String URI, String routeURI,String POST) {
        this.URI = URI;
        this.routeURI = routeURI;
        this.GETMap = ParamHandler.buildGETMap(URI);
        this.POSTMap = ParamHandler.buildPOSTMap(POST);
        this.URIMap = ParamHandler.buildURIMap(URI,routeURI);
        return this.index();
    }
}
