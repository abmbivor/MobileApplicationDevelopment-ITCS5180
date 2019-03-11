package net.ddns.bivor.a801028228_midterm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private class GetSongsAsync extends AsyncTask<Void, Void, ArrayList<Song>> {

        @Override
        protected ArrayList<Song> doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            ArrayList<Song> result = new ArrayList<>();

            try {
                URL url = new URL("http://api.musixmatch.com/ws/1.1/track.search?q=" + editText.getText().toString().trim().replaceAll("\\s", "+") + "&apikey=1908e74cafedd7131cdaf8cca81c5632");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONObject message = root.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");

                    JSONArray songs = body.getJSONArray("track_list");

                    int a = songs.length();
                    for (int i = 0; i < songs.length(); i++) {
                        JSONObject songJson = songs.getJSONObject(i);
                        JSONObject track = songJson.getJSONObject("track");
                        Song song = new Song();
                        song.trackName = (songJson.has("trackName")) ? songJson.getString("trackName") : "N/A";
                        song.artistName = (songJson.has("artistName")) ? songJson.getString("artistName") : "N/A";
                        song.collectionName = (songJson.has("collectionName")) ? songJson.getString("collectionName") : "N/A";
                        song.primaryGenreName = (songJson.has("primaryGenreName")) ? songJson.getString("primaryGenreName") : "N/A";
                        //song.trackPrice = songJson.getString("trackPrice");
                        song.trackPrice = (songJson.has("trackPrice")) ? songJson.getString("trackPrice") : "0";
//                        song.collectionPrice = songJson.getString("collectionPrice");
                        song.collectionPrice = (songJson.has("collectionPrice")) ? songJson.getString("collectionPrice") : "0";
                        song.dateTime = (songJson.has("releaseDate")) ? songJson.getString("releaseDate") : "N/A";
                        song.artworkUrl100 = (songJson.has("artworkUrl100")) ? songJson.getString("artworkUrl100") : "N/A";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        song.date = (songJson.has("releaseDate")) ? sdf.parse(songJson.getString("releaseDate")) : null;
                        song.state = filter;

                        result.add(song);
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                //Close the connections
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Song> result) {

        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

}
