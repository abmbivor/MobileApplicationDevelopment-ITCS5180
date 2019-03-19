package net.ddns.bivor.group14_hw03;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class Song implements Serializable {

    String trackName, primaryGenreName, artistName, collectionName, trackPrice, collectionPrice, dateTime, artworkUrl100;
    Date date;
    String state;

    public Song() {
    }

    public Song(String trackName, String primaryGenreName, String artistName, String collectionName, String trackPrice, String collectionPrice, String dateTime,String artworkUrl100, Date date, String state) {
        this.trackName = trackName;
        this.primaryGenreName = primaryGenreName;
        this.artistName = artistName;
        this.collectionName = collectionName;
        this.trackPrice = trackPrice;
        this.collectionPrice = collectionPrice;
        this.dateTime = dateTime;
        this.artworkUrl100 = artworkUrl100;
        this.date = date;
        this.state = state;
    }

    public static Comparator<Song> COMPARE_BY_PRICE = new Comparator<Song>() {
        public int compare(Song one, Song other) {
            Float a = Float.parseFloat(one.trackPrice);
            Float b = Float.parseFloat(other.trackPrice);
            return a.compareTo(b);
        }
    };

    public static Comparator<Song> COMPARE_BY_DATE = new Comparator<Song>() {
        public int compare(Song one, Song other) {
            return one.date.compareTo(other.date);
        }
    };
}
