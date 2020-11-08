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


public class ViewStudentsController extends RouteController {

    public ViewStudentsController(){}

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
        ht.put("students",DOMStudent.toHashMap());
        if (GETMap.containsKey("success"))
            ht.put("success",GETMap.get("success"));
        if (GETMap.containsKey("error"))
            ht.put("error",GETMap.get("error"));
        RututuBody handler = new RututuBody(ht);
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse("./src/main/resources/views/pages/viewStudents.xml", handler);
        return handler.getHtmlCorps();
    }
}
