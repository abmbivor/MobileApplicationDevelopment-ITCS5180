package net.ddns.bivor.group14_inclass12;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Trip implements Serializable {

    String tripName;
    Place destination = new Place();
    ArrayList<Place> places = new ArrayList<>();
    Date date;
    String datePicked;

    public Trip() {
    }

    public Trip(String tripName, Place destination, ArrayList<Place> places) {
        this.tripName = tripName;
        this.destination = destination;
        this.places = places;
    }

}
