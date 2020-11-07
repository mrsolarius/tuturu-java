package net.proximastro.webserveur.sax;

import net.proximastro.webserveur.sax.component.XMLForEach;
import net.proximastro.webserveur.sax.component.XMLif;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SAXBody extends DefaultHandler {
    private HashMap<String, Object> parms;
    private StringBuilder htmlCorps;

    private String path;
    private XMLif xmlIf;
    private XMLForEach xmlForEach;

    public SAXBody(HashMap<String, Object> parms) {
        super();
        htmlCorps = new StringBuilder();
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
            String valeur = attributes.getValue(i);
            Matcher m = Pattern.compile("(\\$\\{.+?})").matcher(valeur);
            while(m.find()){
                String key = m.group().replace("${","").replace("}","");
                if (this.parms.containsKey(key))
                    valeur = valeur.replace(m.group(), String.valueOf(this.parms.get(key))
                            .replace("\"","&quot;")
                            .replace("'","&apos;")
                            .replace("<","&lt;")
                            .replace(">","&gt;")
                            .replace("&","&amp;"));
            }
            tempHtmlCorp.append(" ").append(attributes.getQName(i)).append("=\"").append(valeur).append("\"");
        }
        tempHtmlCorp.append(">");

        path += "/" + qName;
        if (xmlForEach != null) {
            xmlForEach.appendXML(tempHtmlCorp.toString());
        }
        if (xmlIf != null){
            xmlIf.appendXML(tempHtmlCorp.toString());
        }
        //Gestion des simple balse HTML
        if (qName.startsWith("rutu:")){
            switch (qName.split("rutu:")[1]) {
                case "stylesheet":
                    break;
                case "script":
                    break;
                case "value-of":
                    if (xmlForEach == null && xmlIf ==null) {
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
                                ArrayList<HashMap<String, Object>> hashMap = (ArrayList<HashMap<String, Object>>) parms.get(String.valueOf(attributes.getValue("select")));
                                xmlForEach = new XMLForEach(path, hashMap);
                            }
                        }
                    }
                    break;
                case "if":
                    if (attributes.getLength() > 0) {
                        if (attributes.getIndex("condition") != -1) {
                            if (xmlIf == null) {
                                xmlIf = new XMLif(path, attributes.getValue("condition"),parms);
                            }
                        }
                    }
                    break;
            }
        }else if(xmlIf == null && xmlForEach == null)
            htmlCorps.append(tempHtmlCorp.toString());

}

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        StringBuilder tempHtmlCorp = new StringBuilder("</").append(qName).append(">");


        if (qName.startsWith("rutu:")) {
            switch (qName.split("rutu:")[1]) {
                case "if":
                    if (xmlIf != null) {
                        if (xmlIf.getUri().equals(path)) {
                            try {
                                htmlCorps.append(xmlIf.handle());
                                xmlIf = null;
                            } catch (ParserConfigurationException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            xmlIf.appendXML(tempHtmlCorp.toString());
                        }
                    }
                    break;
                case "for-each":
                    if (xmlForEach != null) {
                        if (xmlForEach.getUri().equals(path)) {
                            try {
                                htmlCorps.append(xmlForEach.handle());
                                xmlForEach = null;
                            } catch (ParserConfigurationException | IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            xmlForEach.appendXML(tempHtmlCorp.toString());
                        }
                    }
                    break;
            }
        } else if (xmlIf == null && xmlForEach == null)
            htmlCorps.append(tempHtmlCorp.toString());

        if (xmlForEach != null) {
            xmlForEach.appendXML(tempHtmlCorp.toString());
        }
        if (xmlIf != null){
            xmlIf.appendXML(tempHtmlCorp.toString());
        }

        path = path.substring(0, path.length() - (qName.length() + 1));
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(xmlForEach != null){
            xmlForEach.appendXML(new String(ch,start,length));
        }
        if(xmlIf != null){
            xmlIf.appendXML(new String(ch,start,length));
        }
        if(xmlIf == null && xmlForEach == null) {
            htmlCorps.append(new String(ch, start, length));
        }
    }
}
