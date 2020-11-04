package net.proximastro.webserveur.sax;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface XMLDomBuilderInterface {
    public abstract String handle() throws ParserConfigurationException, SAXException, IOException;
}
