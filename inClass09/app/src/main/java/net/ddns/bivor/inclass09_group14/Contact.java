package net.ddns.bivor.inclass09_group14;

import java.io.Serializable;

public class Contact implements Serializable {

    String name, phone, email, imageURL;

    public Contact() {
    }

    public Contact(String name, String phone, String email, String imageURL) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.imageURL = imageURL;
    }
}
