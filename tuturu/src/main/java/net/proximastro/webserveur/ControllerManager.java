package net.proximastro.webserveur;

import net.proximastro.controller.RouteController;
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
import java.util.HashMap;

public class ControllerManager {
    private HashMap<String,Object> routeControllers;

    public ControllerManager(){
        //inisialisation de la hashmap
        this.routeControllers = new HashMap<>();
        try {
            //récupération du fichier des routes
            File file = new File("./tuturu/src/main/resources/routes/routes.xml");
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
                Class<?> cls = Class.forName("net.proximastro.controller."+node.getAttributes().getNamedItem("controler").getNodeValue());
                this.routeControllers.put(node.getAttributes().getNamedItem("path").getNodeValue(),cls);
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (SAXException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getControllerFromPath(String path){
        RouteController routeController = (RouteController) routeControllers.get(path);
        routeController.render("test","test1");
    }
}
