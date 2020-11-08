package net.proximastro.controller;


import net.proximastro.app.RouteController;
import net.proximastro.webserveur.dom.DOMStudent;
import net.proximastro.webserveur.model.Student;
import net.proximastro.webserveur.sax.SAXBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;

public class AddOrUpdateStudentController extends RouteController {

    public AddOrUpdateStudentController(){
    }

    @Override
    protected String index() {

        DOMStudent domStudent = new DOMStudent();
        HashMap<String, Object> Map = new HashMap<>();
        if(this.POSTMap.containsKey("id") && this.POSTMap.containsKey("firstName") && this.POSTMap.containsKey("lastName") && this.POSTMap.containsKey("group")){
            int id = Integer.parseInt(this.POSTMap.get("id"));
            String firstName = this.POSTMap.get("firstName");
            String lastName = this.POSTMap.get("lastName");
            String group = this.POSTMap.get("group");
            Student student = new Student(id, firstName, lastName, group);
            try {
                domStudent.addOrUpdateStudent(student);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (id<0)
                return  "301 url:http://localhost:8080/student/views?success=add";
            else
                return  "301 url:http://localhost:8080/student/views?success=edit";
        }else if(this.URIMap.containsKey("id")){
            int id = Integer.parseInt(URIMap.get("id"));

                Student student = domStudent.getStudentById(id);
                if(student != null){
                    Map.put("id",URIMap.get("id"));
                    Map.put("firstName", student.getFirstName());
                    Map.put("lastName", student.getLastName());
                    Map.put("group", student.getGroup());
                }else{
                    return  "301 url:http://localhost:8080/500.html";
                }
        }
        try {
            return parse(Map);
        }catch (Exception e){
            e.printStackTrace();
            return  "301 url:http://localhost:8080/500.html";
        }
    }


    public String parse(HashMap<String, Object> map) throws SAXException, IOException, ParserConfigurationException {
        SAXBody handler = new SAXBody(map);
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse("./src/main/resources/views/pages/addStudent.xml", handler);
        return handler.getHtmlCorps();
    }
}
