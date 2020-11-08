package net.proximastro.app.rututu.component.struct;

import net.proximastro.app.rututu.RututuBody;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class XMLBuilder {
    private String uri;
    protected StringBuilder tempXMLBuilder;
    private HashMap<String,Object> param;

    public XMLBuilder(String uri) {
        this.uri = uri;
        this.tempXMLBuilder = new StringBuilder("<rutu:doNothing>");
    }

    public String getUri() {
        return uri;
    }

    public void appendXML(String xml){
        tempXMLBuilder.append(xml);
    }

    protected String buildHtml(HashMap<String,Object> param) throws ParserConfigurationException, IOException, SAXException {
        if (!tempXMLBuilder.toString().endsWith("</rutu:doNothing>"))tempXMLBuilder.append("</rutu:doNothing>");
        RututuBody handler = new RututuBody(param);
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        InputSource is = new InputSource(new StringReader(tempXMLBuilder.toString()));
        parser.parse(is, handler);
        return handler.getHtmlCorps();
    }
}
