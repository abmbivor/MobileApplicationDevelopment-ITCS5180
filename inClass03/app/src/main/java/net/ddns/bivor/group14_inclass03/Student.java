package net.ddns.bivor.group14_inclass03;

import java.io.Serializable;

public class Student implements Serializable {

    String avatar, firstName, lastName, studentID, department;
    int rbID;

    public Student(String avatar, String firstName, String lastName, String studentID, String department, int rbID) {
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentID = studentID;
        this.department = department;
        this.rbID = rbID;
    }
    


}
