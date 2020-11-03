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


public class TestController2 extends RouteController {

    public TestController2(){}

    @Override
    public String index() {
        try{
            parse();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "OK";
    }

    public void parse() throws SAXException, IOException, ParserConfigurationException {

        SAXBody handler = new SAXBody();
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse("./src/main/resources/views/pages/listPage.xml", handler);
    }
}
