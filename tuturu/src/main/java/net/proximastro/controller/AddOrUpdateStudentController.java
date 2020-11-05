package net.proximastro.controller;

import net.proximastro.app.RouteController;
import net.proximastro.webserveur.dom.DOMStudent;
import net.proximastro.webserveur.model.Student;

public class AddOrUpdateStudentController extends RouteController {

    @Override
    protected String index() {
        DOMStudent domStudent = new DOMStudent();

        if(this.URIMap.containsKey("id") && this.POSTMap.containsKey("firstName") && this.POSTMap.containsKey("lastName") && this.POSTMap.containsKey("group")){
            int id = Integer.parseInt(this.URIMap.get("id"));
            String firstName = this.URIMap.get("firstName");
            String lastName = this.POSTMap.get("lastName");
            String group = this.POSTMap.get("group");
            Student student = new Student(id, firstName, lastName, group);
            try {
                domStudent.addOrUpdateStudent(student);
            }catch (Exception e){
                e.printStackTrace();
            }
            return "ok";
        }else{
            return "echec";
        }
    }
}
