package net.proximastro.controller;

import net.proximastro.app.ControllerManager;
import net.proximastro.app.RouteController;
import net.proximastro.webserveur.dom.DOMStudent;
import net.proximastro.webserveur.sax.SAXBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;

public class ViewHomeController extends RouteController {

    @Override
    public String index() {
        try{
            return parse();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "err";
    }

    public String parse() throws SAXException, IOException, ParserConfigurationException {
        HashMap<String,Object> ht = new HashMap<String,Object>();
        SAXBody handler = new SAXBody(ht);
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse("./src/main/resources/views/pages/viewHome.xml", handler);
        return handler.getHtmlCorps();
    }
}
