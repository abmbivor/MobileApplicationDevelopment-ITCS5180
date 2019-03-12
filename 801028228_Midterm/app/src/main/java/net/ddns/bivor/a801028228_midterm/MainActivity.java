package net.ddns.bivor.a801028228_midterm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
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

    SeekBar sb;
    Button buttonSearch;
    RadioGroup rg;
    RecyclerView recyclerView;
    RecyclerView.Adapter  mAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView textViewLimit;
    TextView test;
    ProgressBar pb;
    EditText editText;
    String filter="&s_track_rating=desc";
    String keyword="";
    int progressValue=5;

    ArrayList<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MusixMatch Track Search");

        tracks = new ArrayList<>();

        sb = findViewById(R.id.seekBar);

        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        rg = findViewById(R.id.radioGroup);

        buttonSearch = findViewById(R.id.buttonSearch);

        textViewLimit = findViewById(R.id.textViewLimit);

        editText = findViewById(R.id.editText);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new TrackAdapter(tracks);
        recyclerView.setAdapter(mAdapter);



        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textViewLimit.setText("Limit: "+ (progress+5));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                pb.setVisibility(View.VISIBLE);
                buttonSearch.setEnabled(false);
                rg.setEnabled(false);
                sb.setEnabled(false);

                if(checkedId==R.id.radioButtonTrack)filter = "&s_track_rating=desc";
                else if (checkedId == R.id.radioButtonArtist)filter ="&s_artist_rating=desc";

                new GetTracksAsync().execute();


            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnected()){
                    pb.setVisibility(View.VISIBLE);
                    buttonSearch.setEnabled(false);
                    rg.setEnabled(false);
                    sb.setEnabled(false);

                    if(editText.getText().toString().isEmpty())editText.setError("Enter Keyword/s");
                    else {
                        keyword = editText.getText().toString();
                        progressValue = (sb.getProgress()+5);
                        mAdapter.notifyDataSetChanged();

                        new GetTracksAsync().execute();
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "There is no internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private class GetTracksAsync extends AsyncTask<Void, Void, ArrayList<Track>> {

        @Override
        protected ArrayList<Track> doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            ArrayList<Track> result = new ArrayList<>();

            try {
                URL url = new URL("http://api.musixmatch.com/ws/1.1/track.search?q=" + editText.getText().toString().trim().replaceAll("\\s", "+")
                + "&page_size=" + progressValue
                        + filter
                        + "&apikey=1908e74cafedd7131cdaf8cca81c5632");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
//                int test = connection.getResponseCode();
//                String t = test+"";
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONObject message = root.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");

                    JSONArray tracks = body.getJSONArray("track_list");

                    for (int i = 0; i < tracks.length(); i++) {
                        JSONObject trackJson = tracks.getJSONObject(i);
                        JSONObject trackHeader = trackJson.getJSONObject("track");
                        Track track = new Track();
                        track.track_name =(trackHeader.has("track_name")) ? trackHeader.getString("track_name") : "N/A";
                        track.artist_name =(trackHeader.has("artist_name")) ? trackHeader.getString("artist_name") : "N/A";
                        track.album_name =(trackHeader.has("album_name")) ? trackHeader.getString("album_name") : "N/A";
                        track.updated_time =(trackHeader.has("updated_time")) ? trackHeader.getString("updated_time") : "N/A";
                        track.track_share_url =(trackHeader.has("track_share_url")) ? trackHeader.getString("track_share_url") : "N/A";

                        result.add(track);
                    }
                }
            } catch (IOException e) {
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
        protected void onPostExecute(ArrayList<Track> result) {

            tracks.clear();
            tracks.addAll(result);
            if(tracks.size()>0){
                mAdapter.notifyDataSetChanged();
                pb.setVisibility(View.INVISIBLE);
                buttonSearch.setEnabled(true);
                rg.setEnabled(true);
                sb.setEnabled(true);
            }
            else {
                Toast.makeText(MainActivity.this, "No Tracks Found", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
                buttonSearch.setEnabled(true);
                rg.setEnabled(true);
                sb.setEnabled(true);
            }

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
