package net.proximastro.app.sax.component;

import net.proximastro.app.sax.component.struct.XMLBuilder;
import net.proximastro.app.sax.component.struct.XMLDomBuilderInterface;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class XMLForEach extends XMLBuilder implements XMLDomBuilderInterface {
    private ArrayList<HashMap<String,Object>> paramList;

    public XMLForEach(String uri, ArrayList<HashMap<String,Object>> paramList) {
        super(uri);
        this.paramList = paramList;
    }

    @Override
    public String handle() throws ParserConfigurationException, SAXException, IOException {
        StringBuilder allHtml = new StringBuilder();
        for (HashMap<String,Object> hashMap:this.paramList) {
            allHtml.append(this.buildHtml(hashMap));
        }
        return allHtml.toString();
    }
}
