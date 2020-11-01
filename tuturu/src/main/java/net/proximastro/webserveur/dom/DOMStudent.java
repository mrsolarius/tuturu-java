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
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DOMStudent {
    private static String studentsFileDir = "src/main/resources/data/student_data.xml";
    private static List<Student> studentList;

    public static Document getDocument() throws IOException, SAXException, ParserConfigurationException {
        File studentsFile = new File(studentsFileDir);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(studentsFile);
    }

    public boolean addOrUpdateStudent(Student student) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document document = getDocument();

        Element element = document.getDocumentElement();


        if(student.getId()>0) {
            boolean estModifie = updateStudent(document, student);

            if(!estModifie){
                return false;
            }
        }else{
            addStudent(document, element, student);
        }
        saveXMLContent(document, studentsFileDir);
        return true;
    }

    public boolean deleteStudent(Student student) throws ParserConfigurationException, SAXException, IOException {
        Document document = getDocument();
        document.getDocumentElement().normalize();
        System.out.println("Root element:" + document.getDocumentElement().getNodeName());
        Node studentsNode = document.getElementsByTagName("students").item(0);
        NodeList nodeList = document.getElementsByTagName("student");
        int i = 0;
        for(i=0; i<nodeList.getLength(); i++){
            Node nodeEtudiant = nodeList.item(i);
            Element elementEtudiant = (Element) nodeEtudiant;
            if(elementEtudiant.getAttributeNode("id").getValue().equals(String.valueOf(student.getId()))){
//                nodeList.item(i).removeChild(nodeEtudiant);
                studentsNode.removeChild(nodeEtudiant);
                saveXMLContent(document, studentsFileDir);
                return true;
            }
            i++;
        }
        return false;
    }

    private void addStudent(Document document, Element element, Student student) {
        Element elementStudent = document.createElement("Student");
        elementStudent.setAttribute("id", String.valueOf(studentList.get(studentList.size()-1).getId()+1));
        addChildElement(elementStudent, "firstName", student.getFirstName());
        addChildElement(elementStudent, "lastName", student.getLastName());
        addChildElement(elementStudent, "group", student.getGroup());

        element.appendChild(elementStudent);
    }

    private boolean updateStudent(Document document, Student student) {
        document.getDocumentElement().normalize();
        System.out.println("Root element:" + document.getDocumentElement().getNodeName());
        NodeList nodeList = document.getElementsByTagName("student");
        int i = 0;
        for(i=0; i<nodeList.getLength(); i++){
            Element elementEtudiant = (Element) nodeList.item(i);
            if(elementEtudiant.getAttributeNode("id").getValue().equals(String.valueOf(student.getId()))){
                Node firstName = elementEtudiant.getElementsByTagName("firstName").item(0).getFirstChild();
                firstName.setNodeValue(student.getFirstName());

                Node lastName = elementEtudiant.getElementsByTagName("lastName").item(0).getFirstChild();
                lastName.setNodeValue(student.getLastName());

                Node group = elementEtudiant.getElementsByTagName("group").item(0).getFirstChild();
                group.setNodeValue(student.getGroup());
                return true;
            }
            i++;
        }
        return false;
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

    private static void saveXMLContent(Document document, String xmlFile) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(xmlFile);
            transformer.transform(domSource, streamResult);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void start () throws IOException, SAXException, ParserConfigurationException, TransformerException {
        this.studentList = getListStudents();

        for(Student s : this.studentList){
            System.out.println(s.toString());
        }

        addOrUpdateStudent(new Student(3, "jeantet", "Joey", "tuturu"));
        deleteStudent(new Student(5, "test", "test","test"));
    }

}
