package net.proximastro.webserveur.sax;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class TempXMLDomBuilder {
    private String uri;
    private StringBuilder tempXMLBuilder;
    private Document xml;

    public TempXMLDomBuilder(String uri, String tempXMLBuilder) {
        this.uri = uri;
        this.tempXMLBuilder = new StringBuilder(tempXMLBuilder);
    }

    public void appendXML(String xml){
        tempXMLBuilder.append(xml);
    }

    public void buildDom() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(tempXMLBuilder.toString()));
        this.xml = db.parse(is);
    }
}
