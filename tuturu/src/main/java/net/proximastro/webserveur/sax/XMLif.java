package net.proximastro.webserveur.sax;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

public class XMLif extends XMLBuilder implements XMLDomBuilderInterface {
    private String condition;
    HashMap<String,Object> hashMap;

    public XMLif(String uri,String condition, HashMap<String,Object> param) {
        super(uri);
        this.condition = condition;
        this.hashMap = param;
    }

    @Override
    public String handle() throws ParserConfigurationException, SAXException, IOException {
        System.out.println(tempXMLBuilder.toString());
        String[] arr = tempXMLBuilder.toString().split("<rutu:else></rutu:else>");
        boolean var = true;
        if (var == true){
            tempXMLBuilder = new StringBuilder(arr[0]);
        }else {
            tempXMLBuilder = new StringBuilder(arr[1]);
        }
        return this.buildHtml(this.hashMap);
    }
}
