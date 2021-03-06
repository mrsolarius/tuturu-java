package net.proximastro.webserveur.model;

public class Student {
    private int id;
    private String name;
    private String surname;
    private String group;

    public Student(int id, String name, String surname, String group) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return name;
    }

    public void setFirstName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return surname;
    }

    public void setLastName(String surname) {
        this.surname = surname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String toString(){
        return "id : " + id + "\n" +
                "first name : " + name + "\n" +
                "last name : " + surname + "\n" +
                "group : " + group;
    }
}