package net.ddns.bivor.group14_ic10;

import java.io.Serializable;

public class Note implements Serializable {

    String id,userID,text;
    int __v;

    public Note(String id, String userID, String text, int __v) {
        this.id = id;
        this.userID = userID;
        this.text = text;
        this.__v = __v;
    }

    public Note() {
    }
}
