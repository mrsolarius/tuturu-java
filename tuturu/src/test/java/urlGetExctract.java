import java.util.HashMap;

public class urlGetExctract {
    public static void main(String[] args) {
        String uri = "/test/1/4/bonjour?truc=2&test=1";
        String route = "/test/{catalogue}/{personne}/bonjour";

        //Récupération de la parti URL uniquement
        String justURI = uri.split("\\?+")[0];
        //test de si il y a une methode get dans l'uri
        if (uri.split("\\?+").length==2) {
            //inisialisation de la hash map des paramettres
            HashMap<String, String> getParams = new HashMap<>();

            //Sépartion de tous les parametre de la requette get
            String[] params = uri.split("\\?+")[1].split("&");
            for (int i = 0; i < params.length; i++) {
                //definition du tupe de valeur cles et valeur
                String[] keyValue = params[i].split("=");
                //verification qu'il y a bien une valeur apres le =
                if (keyValue.length == 2) {
                    //ajouts des paramettre à la hashmap
                    getParams.put(keyValue[0], keyValue[1]);
                } else {
                    System.out.println("Requete GET invalide");
                }
            }
        }

        String[] uriArr = justURI.split("/");
        String[] routeArr = route.split("/");
        Boolean flag=true;
        if (uriArr.length == routeArr.length){
            for (int i = 0; i <uriArr.length ; i++) {
                if (!(routeArr[i].startsWith("{") && routeArr[i].endsWith("}"))){
                    System.out.println(routeArr[i]);
                    System.out.println(uriArr[i]);
                    if (!uriArr[i].equals(routeArr[i])) flag = false;
                }
            }
        }

        System.out.println(flag);




    }

    public static void printArray(String[] list){
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }
    }
}
