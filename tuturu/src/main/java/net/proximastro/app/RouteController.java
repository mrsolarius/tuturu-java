package net.proximastro.app;

import java.util.HashMap;

public class RouteController {
    private HashMap<String,String> GETMap;
    private HashMap<String,String> URIMap;
    private String URI;
    private String routeURI;

    public RouteController(){}

    private String index(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title> Page par defaut</title>\n" +
                "</head>\n" +
                "<body>" +
                "   <h1>Données</h1>" +
                "   <h2>URI</h2>" +
                "   <p>Route URI : <code>"+routeURI+"</code></p>" +
                "   <p>Local URI : <code>"+URI+"</code></p>" +
                "   <h2>Get Parameters</h2>" +
                "   <p>HashMap GET : <code>"+GETMap.toString()+"</code></p>" +
                "   <p>HashMap URI : <code>"+URIMap.toString()+"</code></p>" +
                "</body>" +
                "</html>";
    }

    public String render(String URI, String routeURI) {
        this.URI = URI;
        this.routeURI = routeURI;
        this.GETMap = buildGETMap(URI);
        this.URIMap = buildURIMap(URI,routeURI);
        return this.index();
    }

    public static HashMap<String,String> buildGETMap(String URI){
        //inisialisation de la hash map des paramettres
        HashMap<String, String> getParams = new HashMap<>();
        if (URI.split("\\?+").length==2) {
            //Sépartion de tous les parametre de la requette get
            String[] params = URI.split("\\?+")[1].split("&");
            for (int i = 0; i < params.length; i++) {
                //definition du tupe de valeur cles et valeur
                String[] keyValue = params[i].split("=");
                //verification qu'il y a bien une valeur apres le =
                if (keyValue.length == 2) {
                    //ajouts des paramettre à la hashmap
                    getParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return getParams;
    }

    public static HashMap<String,String> buildURIMap(String URI, String routeURI){
        //Séparation des routes en tableau d'élement
        String[] URIArray = URI.split("\\?+")[0].split("/");
        String[] routeURIArray = routeURI.split("/");
        //inisialisation de la hashmap
        HashMap<String,String> URIMap = new HashMap<>();
        if (URIArray.length == routeURIArray.length){
            for (int i = 0; i <URIArray.length ; i++) {
                //Si la route et une variable
                if (routeURIArray[i].startsWith("{") && routeURIArray[i].endsWith("}")){
                    //Alors on renplie la hash map
                    URIMap.put(
                            routeURIArray[i]
                                    .replace("{","")
                                    .replace("}",""),
                            URIArray[i]
                    );
                }
            }
        }
        return  URIMap;
    }
}
