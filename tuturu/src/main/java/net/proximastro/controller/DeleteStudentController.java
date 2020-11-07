package net.proximastro.controller;

import net.proximastro.app.RouteController;
import net.proximastro.webserveur.dom.DOMStudent;
import net.proximastro.webserveur.model.Student;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

public class DeleteStudentController extends RouteController {
    public DeleteStudentController(){}

    @Override
    protected String index() {
        if (URIMap.containsKey("id")){
            try {
                if (delete()) {
                    return "301 url:http://localhost:8080/student/views?success=delete";
                }else {
                    return "301 url:http://localhost:8080/student/views?error=delete";
                }
            }catch (Exception e){
                return  "301 url:http://localhost:8080/500.html";
            }
        }else {
            return "301 url:http://localhost:8080/404.html";
        }
    }

    public boolean delete() throws SAXException, IOException, ParserConfigurationException {
        HashMap<String,Object> ht = new HashMap<String,Object>();
        Student student = new Student(Integer.parseInt(URIMap.get("id")) ,"","","");
        return DOMStudent.deleteStudent(student);
    }
}
