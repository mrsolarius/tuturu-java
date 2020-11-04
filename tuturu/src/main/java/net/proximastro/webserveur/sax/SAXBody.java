package net.proximastro.webserveur.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SAXBody extends DefaultHandler {
    private HashMap<String, Object> parms;
    private StringBuilder htmlCorps;

    private String path;

    private IfClass ifContainer;
    private XMLForEach xmlForEach;

    public SAXBody(HashMap<String, Object> parms) {
        super();
        htmlCorps = new StringBuilder();
        ifContainer = new IfClass();
        this.parms = parms;
        path = "";
    }

    public String getHtmlCorps() {
        return htmlCorps.toString();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("end document");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        StringBuilder tempHtmlCorp = new StringBuilder("<").append(qName);
        for (int i = 0; i < attributes.getLength(); i++) {
            tempHtmlCorp.append(" ").append(attributes.getQName(i)).append("=\"").append(attributes.getValue(i)).append("\"");
        }
        tempHtmlCorp.append(">");

        path += "/" + qName;
        System.out.println(path);
        if (xmlForEach!=null){
            xmlForEach.appendXML(tempHtmlCorp.toString());
        }
        if (ifContainer.getValueOfLastHashMap()) {
            //Gestion des simple balse HTML
            if (!qName.startsWith("rutu:")) {
                htmlCorps.append(tempHtmlCorp.toString());
            } else {
                switch (qName.split("rutu:")[1]) {
                    case "stylesheet":
                        break;
                    case "script":
                        break;
                    case "value-of":
                        if (xmlForEach==null){
                            if (attributes.getLength() > 0) {
                                if (attributes.getIndex("select") != -1) {
                                    if (!String.valueOf(this.parms.get(attributes.getValue("select"))).isEmpty()) {
                                        htmlCorps.append(this.parms.get(attributes.getValue("select")));
                                    }
                                }
                            }
                        }
                        break;
                    case "for-each":
                        if (attributes.getLength() > 0) {
                            if (attributes.getIndex("select") != -1) {
                                if (xmlForEach == null) {
                                    System.out.println(attributes.getValue("select"));
                                    ArrayList<HashMap<String, Object>> hashMap = (ArrayList<HashMap<String, Object>>) parms.get(String.valueOf(attributes.getValue("select")));
                                    xmlForEach = new XMLForEach(path, hashMap);
                                }
                            }
                        }
                        break;
                    case "if":
                        if (attributes.getLength() > 0) {
                            if (attributes.getIndex("select") != -1) {
                                if (attributes.getValue("select").equals("true")) {
                                    ifContainer.addIfOnHashMap(path, true);
                                } else if (attributes.getValue("select").equals("false")) {
                                    ifContainer.addIfOnHashMap(path, false);
                                }
                            }
                        }
                        break;
                    case "else":
                        ifContainer.inverseValueOfLastHashMap();
                        break;
                }
            }
            System.out.println(htmlCorps.toString());
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        StringBuilder tempHtmlCorp = new StringBuilder("</").append(qName).append(">");


        if (ifContainer.getValueOfLastHashMap()) {
            if (qName.startsWith("rutu:")) {
                switch (qName.split("rutu:")[1]) {
                    case "if":
                        ifContainer.removeLastHashMap();
                        break;
                    case "for-each":
                        if (xmlForEach != null) {
                            if (xmlForEach.getUri().equals(path)) {
                                try {
                                    htmlCorps.append(xmlForEach.handle());
                                } catch (ParserConfigurationException | IOException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                xmlForEach.appendXML(tempHtmlCorp.toString());
                            }
                        }
                        xmlForEach = null;
                        break;
                }
            } else {

                htmlCorps.append("</").append(qName).append(">");
                System.out.println("</" + qName + ">");
            }
        } else {
            if (qName.startsWith("rutu:")) {
                switch (qName.split("rutu:")[1]) {
                    case "else":
                        ifContainer.inverseValueOfLastHashMap();
                        break;
                    case "for-each":
                        if (xmlForEach != null) {
                            if (xmlForEach.getUri().equals(path)) {
                                try {
                                    htmlCorps.append(xmlForEach.handle());
                                } catch (ParserConfigurationException | IOException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                xmlForEach.appendXML(tempHtmlCorp.toString());
                            }
                        }
                        xmlForEach = null;
                        break;

                }
            }
        }

        if (xmlForEach!=null){
            xmlForEach.appendXML(tempHtmlCorp.toString());
        }

        path = path.substring(0, path.length() - (qName.length() + 1));
        System.out.println(path);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }
}
