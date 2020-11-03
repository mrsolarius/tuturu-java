package net.proximastro.webserveur.sax;

import javax.swing.text.Document;
import java.util.ArrayList;

public class XMLForEatch extends TempXMLDomBuilder implements XMLDomBuilderInterface{
    private String uri;
    private String tempXMLBuilder;
    private Document xml;
    private ArrayList<Object> forEatchList;

    public XMLForEatch(String uri, String tempXMLBuilder, ArrayList<Object> forEatchList) {
        super(uri,tempXMLBuilder);
        this.forEatchList = forEatchList;
    }

    @Override
    public String handle() {
        return null;
    }
}
