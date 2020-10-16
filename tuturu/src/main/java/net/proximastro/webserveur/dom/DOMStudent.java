package net.proximastro.webserveur.dom;

import net.proximastro.webserveur.model.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DOMStudent {
    private static String studentsFileDir = "tuturu/src/main/resources/data/student_data.xml";

    public static Document getDocument() throws IOException, SAXException, ParserConfigurationException {
        File studentsFile = new File(studentsFileDir);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(studentsFile);
    }

    public static void addStudent(Student student) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document document = getDocument();

        Element element = document.getDocumentElement();

        addChildElement(element, "student", "test");


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(studentsFileDir));
        transformer.transform(source, result);

        /*
        Element child = document.createElement("student");
        child.setAttribute("id", "5");
        String text = "test";

        element.appendChild(child);

        /*
        Element child = document.createElement(name);
        element.appendChild(child);
        if (textValue != null) {
            String text = textValue.toString();
            child.appendChild(document.createTextNode(text));
        }
         */
    }

    public static void addChildElement(Element element, String name, Object
            textValue) {
        Document document = element.getOwnerDocument();
        Element child = document.createElement(name);
        element.appendChild(child);
        if (textValue != null) {
            String text = textValue.toString();
            child.appendChild(document.createTextNode(text));
        }
    }

    public static ArrayList<Student> getListStudents(){
        ArrayList<Student> listStudents = new ArrayList<>();

        try {
            Document doc = getDocument();
            doc.getDocumentElement().normalize();
            System.out.println("Root element:" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("student");
            for(int i=0; i<nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) node;

                    int id = Integer.parseInt(node.getAttributes().getNamedItem("id").getNodeValue());
                    String firstName = e.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = e.getElementsByTagName("lastName").item(0).getTextContent();
                    String group = e.getElementsByTagName("group").item(0).getTextContent();

                    listStudents.add(new Student(id, firstName, lastName, group));
                }
            }

            return listStudents;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main (String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        ArrayList<Student> listStudents = getListStudents();

        for(Student s : listStudents){
            System.out.println(s.toString());
        }

        addStudent(new Student(50, "nom", "prenom", "tuturu"));
    }

}
