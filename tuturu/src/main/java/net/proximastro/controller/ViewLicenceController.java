package net.proximastro.controller;

import net.proximastro.app.RouteController;
import net.proximastro.app.rututu.RututuBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;

import static net.proximastro.Path.getPath;

public class ViewLicenceController extends RouteController {

    @Override
    public String index() {
        try{
            return parse();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "err";
    }

    public String parse() throws SAXException, IOException, ParserConfigurationException {
        HashMap<String,Object> ht = new HashMap<String,Object>();
        RututuBody handler = new RututuBody(ht);
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse(getPath()+"views/pages/viewLicence.xml", handler);
        return handler.getHtmlCorps();
    }
}
