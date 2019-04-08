package net.ddns.bivor.group14_hw06;

import java.io.Serializable;

public class User implements Serializable {

    String firstName, lastName, email, password;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
