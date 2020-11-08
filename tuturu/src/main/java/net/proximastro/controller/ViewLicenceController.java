package net.proximastro.controller;

import net.proximastro.app.RouteController;
import net.proximastro.app.sax.SAXBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;

public class ViewLicenceController extends RouteController {

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
        parser.parse("./src/main/resources/views/pages/viewLicence.xml", handler);
        return handler.getHtmlCorps();
    }
}
