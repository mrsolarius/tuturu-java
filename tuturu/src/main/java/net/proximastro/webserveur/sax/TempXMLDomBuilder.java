package net.proximastro.webserveur.sax;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class TempXMLDomBuilder {
    private String uri;
    private StringBuilder tempXMLBuilder;

    public TempXMLDomBuilder(String uri) {
        this.uri = uri;
        this.tempXMLBuilder = new StringBuilder();
    }

    public String getUri() {
        return uri;
    }

    public void appendXML(String xml){
        tempXMLBuilder.append(xml);
    }

    protected String buildHtml(HashMap<String,Object> param) throws ParserConfigurationException, IOException, SAXException {
        SAXBody handler = new SAXBody(param);
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        System.out.println("=======================");
        System.out.println(tempXMLBuilder.toString());
        System.out.println("=======================");
        InputSource is = new InputSource(new StringReader(tempXMLBuilder.toString()));
        parser.parse(is, handler);
        return handler.getHtmlCorps();
    }
}
