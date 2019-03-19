package net.ddns.bivor.group14_hw02;

import java.io.Serializable;
import java.util.ArrayList;

public class Passwords implements Serializable {

    ArrayList<String> password;

    public Passwords(ArrayList<String> password) {
        this.password = password;
    }

    public ArrayList<String> getPassword() {
        return password;
    }
}
