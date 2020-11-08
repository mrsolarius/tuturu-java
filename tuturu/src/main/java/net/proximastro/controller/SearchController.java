package net.proximastro.controller;

import net.proximastro.app.RouteController;
import net.proximastro.webserveur.dom.DOMStudent;
import net.proximastro.app.rututu.RututuBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;

public class SearchController extends RouteController {
    public SearchController(){}

    @Override
    protected String index() {
        if (URIMap.containsKey("searchExpresion")){
            try{
                return parse();
            }catch (Exception e){
                e.printStackTrace();
            }
            return "301 url:http://localhost/500.html";
        }else {
            return "301 url:http://localhost/404.html";
        }
    }

    public String parse() throws SAXException, IOException, ParserConfigurationException {
        HashMap<String,Object> ht = new HashMap<String,Object>();
        ht.put("students",DOMStudent.searchStudent(URIMap.get("searchExpresion")));
        RututuBody handler = new RututuBody(ht);
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse("./src/main/resources/views/pages/viewStudents.xml", handler);
        return handler.getHtmlCorps();
    }
}
