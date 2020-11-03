package net.proximastro.webserveur.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXBody extends DefaultHandler {
    private String htmlCorps;

    public SAXBody(){
        super();
    }

    public String getHtmlCorps() {
        return htmlCorps;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("start document");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("end document");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        System.out.println("<" + qName + ">");
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        System.out.println("</" + qName + ">");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }
}
