package net.ddns.bivor.group14_hw06;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    String message, imageURL, firstName;
    Date prettyTime;

    public Message() {
    }

    public Message(String message, String imageURL, String firstName, Date prettyTime) {
        this.message = message;
        this.imageURL = imageURL;
        this.firstName = firstName;
        this.prettyTime = prettyTime;
    }
}
