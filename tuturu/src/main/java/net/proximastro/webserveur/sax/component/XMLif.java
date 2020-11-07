package net.proximastro.webserveur.sax.component;

import net.proximastro.webserveur.sax.component.struct.ParseCondition;
import net.proximastro.webserveur.sax.component.struct.XMLBuilder;
import net.proximastro.webserveur.sax.component.struct.XMLDomBuilderInterface;
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
        if (ParseCondition.checkCondition(this.condition,this.hashMap)){
            tempXMLBuilder = new StringBuilder(arr[0]).append("</rutu:doNothing>");
        }else {
            if (arr.length>1)
                tempXMLBuilder = new StringBuilder("<rutu:doNothing>").append(arr[1]);
            else
                tempXMLBuilder = new StringBuilder("<rutu:doNothing>");
        }
        return this.buildHtml(this.hashMap);
    }
}
