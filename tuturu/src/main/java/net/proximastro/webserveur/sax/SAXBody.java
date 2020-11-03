package net.proximastro.webserveur.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;

public class SAXBody extends DefaultHandler {
    private String lang;
    private HashMap<String,Object> parms;
    private StringBuilder htmlCorps;
    private HashMap<String,String> forEatchStr;

    public SAXBody(String lang, HashMap<String,Object> parms){
        super();
        htmlCorps = new StringBuilder();
        forEatchStr = new HashMap<>();
        this.parms = parms;
        this.lang = lang;
    }

    public String getHtmlCorps() {
        return htmlCorps.toString();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        htmlCorps.append("<!DOCTYPE html><html lang=").append(this.lang).append(">");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("end document");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        //Gestion des simple balse HTML
        if (!qName.startsWith("rutu:")){
            htmlCorps.append("<").append(qName);
            for (int i = 0; i < attributes.getLength() ; i++) {
                htmlCorps.append(" ").append(attributes.getQName(i)).append("=\"").append(attributes.getValue(i)).append("\"");
            }
            htmlCorps.append(">");
        //Gestion des balise de templating
        }else {
            switch (qName.split("rutu:")[1]){
                case "stylesheet":
                    break;
                case "script":
                    break;
                case "value-of":
                    if (attributes.getLength()>0){
                        if (attributes.getIndex("select")!=-1){
                            if (this.parms.get(attributes.getValue("select"))!=null){
                                htmlCorps.append(this.parms.get(attributes.getValue("select")));
                            }
                        }
                    }
                    break;
                case "for-each":
                    if (attributes.getLength()>0) {
                        if (attributes.getIndex("select") != -1) {
                        }
                    }
                    break;
                case "if":
                    break;
                case "else":
                    break;
            }
        }
        System.out.println(htmlCorps.toString());
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        htmlCorps.append("</").append(qName).append(">");
        System.out.println("</" + qName + ">");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }
}
