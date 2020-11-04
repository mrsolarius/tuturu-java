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
import java.util.HashMap;


public class TestController2 extends RouteController {

    public TestController2(){}

    @Override
    public String index() {
        try{
            return parse();
        }catch (Exception e){
            StringBuilder strToReturn = new StringBuilder();
            for(StackTraceElement at : e.getStackTrace()){
                strToReturn.append(at.toString());
            }
            return strToReturn.toString();
        }
    }

    public String parse() throws SAXException, IOException, ParserConfigurationException {

        SAXBody handler = new SAXBody("xml", new HashMap<>());
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse("./src/main/resources/views/pages/listPage.xml", handler);
        return handler.getHtmlCorps();
    }
}
