package net.proximastro.app;

import java.util.HashMap;

class ParamHandler {
    static HashMap<String,String> buildGETMap(String URI){
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

    static HashMap<String,String> buildPOSTMap(String POST){
        //inisialisation de la hash map des paramettres
        HashMap<String, String> postParams = new HashMap<>();
        //Sépartion de tous les parametre de la requette get
        String[] params = POST.split("&");
        for (String param : params) {
            //definition du tupe de valeur cles et valeur
            String[] keyValue = param.split("=");
            //verification qu'il y a bien une valeur apres le =
            if (keyValue.length == 2) {
                //ajouts des paramettre à la hashmap
                postParams.put(keyValue[0], keyValue[1]);
            }
        }
        return postParams;
    }

    static HashMap<String,String> buildURIMap(String URI, String routeURI){
        //Séparation des routes en tableau d'élement
        String[] URIArray = URI.split("\\?+")[0].split("/");
        String[] routeURIArray = routeURI.split("/");
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
