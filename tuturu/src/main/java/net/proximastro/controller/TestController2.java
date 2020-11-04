package net.proximastro.controller;

import net.proximastro.app.RouteController;
import net.proximastro.webserveur.sax.SAXBody;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.ParserFactory;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class TestController2 extends RouteController {

    public TestController2(){}

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
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        list.add(this.GETMap);
        list.add(this.GETMap);
        list.add(this.GETMap);
        list.add(this.GETMap);
        list.add(this.GETMap);
        list.add(this.GETMap);
        ht.put("get",list);
        SAXBody handler = new SAXBody(ht);
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse("./src/main/resources/views/pages/listPage.xml", handler);
        return handler.getHtmlCorps();
    }
}
