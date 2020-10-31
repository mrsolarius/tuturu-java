package net.proximastro.app;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ControllerManager {
    static final String ROUTES_EMPLACEMENT = "./tuturu/src/main/resources/routes/routes.xml";
    private HashMap<String,RouteController> routeControllers;

    public ControllerManager(){
        //inisialisation de la hashmap
        this.routeControllers = new HashMap<>();
        try {
            //récupération du fichier des routes
            File file = new File(ROUTES_EMPLACEMENT);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            //récupération du DOM
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("route");

            //parcoures des routes
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                String controllerName = node.getAttributes().getNamedItem("controler").getNodeValue();
                String path = node.getAttributes().getNamedItem("path").getNodeValue();
                ArrayList<String> methods = new ArrayList<>(
                        Arrays.asList(
                                node.getAttributes()
                                    .getNamedItem("method")
                                    .getNodeValue()
                                    .split("\\|")
                        )
                );
                try {
                    Class<?> clazz = Class.forName("net.proximastro.controller." + controllerName);
                    Constructor<?> ctor = clazz.getConstructor();
                    Object object = ctor.newInstance();
                    RouteController rc = (RouteController) object;
                    rc.setHandleMethods(methods);
                    this.routeControllers.put(path, rc);
                }catch (ClassNotFoundException e){
                    System.out.println("Le controleur : '"+controllerName+"' idiquer dans la route :'"+path+"' n'as pas était trouver");
                    e.printStackTrace();
                }catch (InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                    System.out.println("Imposable d'instentier le controller : '"+controllerName+"'");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    System.out.println("Imposable d'accéder à la class : '"+controllerName+"' depuis la class : '"+this.getClass().getName()+"'");
                    e.printStackTrace();
                }
            }

        }catch (FileNotFoundException e) {
            System.out.println("Le ficheir de configuration des route ne se trouve pas au path atendu");
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            System.out.println("Une erreur dans la configuration du parseur XML à eu lieux");
            e.printStackTrace();
        }catch (SAXException e){
            System.out.println("Le ficheir de routes.xml n'est pas corectement former");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("Erreur lors de la lecture de l'entrer");
            e.printStackTrace();
        }
    }

    public String getRegisterRoutesURI(String URI,String method){
        String routeURI="";
        //récupération de l'uri nettoyer d'eventuelle methode GET et transphormation en tableau
        String[] uriArr = URI.split("\\?+")[0].split("/");

        //parcours des cles de la hash map
        for (String key : this.routeControllers.keySet()) {
            //Si le controler peut handle la methode utiliser
            if (this.routeControllers.get(key).getHandleMethods().contains(method)){
                //récupération du tableau de la route
                String[] routeArr = key.split("/");

                //si les deux tableau ne font pas la même taille alors ce n'est pas
                if (uriArr.length == routeArr.length){
                    //récupération de la cles de la route
                    routeURI = key;
                    for (int i = 0; i <uriArr.length ; i++) {
                        //Si l'élement n'est pas une variable de route
                        if (!(routeArr[i].startsWith("{") && routeArr[i].endsWith("}"))){
                            //si les deux element ne sont pas exactement egal
                            if (!uriArr[i].equals(routeArr[i])){
                                //on reinisialise la valeur de la cles
                                routeURI = "";
                                break;
                            };
                        }
                    }
                }
            }
            //Si la vzaleur de la cles na pas était réinisialisée alors on renvoie la cles
            if (!routeURI.equals("")) return routeURI;
        }
        return routeURI;
    }

    public RouteController getRouteController(String routeURI){
        return this.routeControllers.get(routeURI);
    }
}
