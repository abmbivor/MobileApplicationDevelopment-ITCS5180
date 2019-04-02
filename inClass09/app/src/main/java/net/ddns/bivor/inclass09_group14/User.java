package net.ddns.bivor.inclass09_group14;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    String firstName, lastName, email, password;
    ArrayList<Contact> contacts;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, ArrayList<Contact> contacts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.contacts = contacts;
    }
}
