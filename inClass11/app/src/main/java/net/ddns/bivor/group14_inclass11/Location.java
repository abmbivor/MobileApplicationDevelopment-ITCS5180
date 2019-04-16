package net.ddns.bivor.group14_inclass11;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Location {

    ArrayList<Loc> points;
    String title;

    public Location(ArrayList<Loc> points, String title) {
        this.points = points;
        this.title = title;
    }

    public ArrayList<Loc> getPoints() {
        return points;
    }
}
