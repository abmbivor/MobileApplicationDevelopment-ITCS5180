package net.ddns.bivor.a801028228_midterm;

import java.io.Serializable;

public class Track implements Serializable {

    String track_name, album_name, artist_name, updated_time, track_share_url;

    public Track() {
    }

    public Track(String track_name, String album_name, String artist_name, String updated_time, String track_share_url) {
        this.track_name = track_name;
        this.album_name = album_name;
        this.artist_name = artist_name;
        this.updated_time = updated_time;
        this.track_share_url = track_share_url;
    }
}
