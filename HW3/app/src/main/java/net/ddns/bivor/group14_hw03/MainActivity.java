package net.ddns.bivor.group14_hw03;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    SeekBar sb;
    Button buttonSearch, buttonReset;
    Switch switchFilter;
    RecyclerView  recyclerView;
    RecyclerView.Adapter  mAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView textViewLimit, textViewSortBy, textViewResults, textViewDate;
    TextView test;
    ProgressBar pb;
    EditText editText;
    String filter="date";
    ArrayList<Song> songs;
    String keyword="";
    int progressValue=10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("iTunes Music Search");

        songs = new ArrayList<>();

        sb = findViewById(R.id.seekBar);
        switchFilter = findViewById(R.id.switchFilter);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        buttonSearch = findViewById(R.id.buttonSearch);
        buttonReset = findViewById(R.id.buttonReset);


        textViewLimit = findViewById(R.id.textViewLimit);
        textViewSortBy = findViewById(R.id.textView2);
        textViewResults = findViewById(R.id.textView3);
        textViewDate = findViewById(R.id.textView4);
        //test = findViewById(R.id.textView2);
        editText = findViewById(R.id.editText);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        mAdapter = new SongAdapter(songs,keyword,filter,progressValue);
        mAdapter = new SongAdapter(songs);
        recyclerView.setAdapter(mAdapter);

        if(getIntent()!=null && getIntent().getExtras() != null) {
            songs.addAll ((ArrayList<Song>) getIntent().getExtras().getSerializable(Display.SONGS_KEY2));
//            if(songs.get(0).state =="date") {
//                if (!switchFilter.isChecked())switchFilter.toggle();
//            }
//            else {
//                if (switchFilter.isChecked())switchFilter.toggle();
//            }
//            keyword = getIntent().getExtras().getString(Display.KEYWORD2);
//            filter = getIntent().getExtras().getString(Display.STATE2);
//            progressValue = getIntent().getExtras().getInt(Display.PROGRESS_VALUE2);
//            sb.setProgress(progressValue);
//            textViewLimit.setText("Limit: "+ progressValue);
//            editText.setText(keyword);
//            if(filter=="date")switchFilter.setChecked(true);
//            else switchFilter.setChecked(false);
            mAdapter.notifyDataSetChanged();
        }


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textViewLimit.setText("Limit: "+ (progress+10));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        switchFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    filter="price";
                    Collections.sort(songs, Song.COMPARE_BY_PRICE);
                    songs.get(0).state="price";
                    mAdapter.notifyDataSetChanged();
                }
                else{
                    filter="date";
                    Collections.sort(songs,Song.COMPARE_BY_DATE);
                    songs.get(0).state="date";
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnected()){
                    if(editText.getText().toString().isEmpty())editText.setError("Enter Keyword/s");
                    else {
                        keyword = editText.getText().toString();
                        progressValue = (sb.getProgress()+10);
                        mAdapter.notifyDataSetChanged();
                        disableAll();
//                        songs.clear();
                        new GetSongsAsync().execute();
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "There is no internet connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                sb.setProgress(0);
//                Intent intent = new Intent(MainActivity.this, Display.class);
//                startActivity(intent);
            }
        });
    }

    private class GetSongsAsync extends AsyncTask<Void, Void, ArrayList<Song>> {

        @Override
        protected ArrayList<Song> doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            ArrayList<Song> result = new ArrayList<>();

            try {
                URL url = new URL("https://itunes.apple.com/search?term="+ editText.getText().toString().trim().replaceAll("\\s","+") + "&limit=" + (sb.getProgress()+10));
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray songs = root.getJSONArray("results");
                    int a = songs.length();
                    for (int i=0;i<songs.length();i++) {
                        JSONObject songJson = songs.getJSONObject(i);
                        Song song = new Song();
                        song.trackName = (songJson.has("trackName"))? songJson.getString("trackName"):"N/A";
                        song.artistName = (songJson.has("artistName"))? songJson.getString("artistName"):"N/A";
                        song.collectionName = (songJson.has("collectionName"))? songJson.getString("collectionName"):"N/A";
                        song.primaryGenreName = (songJson.has("primaryGenreName"))? songJson.getString("primaryGenreName"):"N/A";
                        //song.trackPrice = songJson.getString("trackPrice");
                        song.trackPrice = (songJson.has("trackPrice"))? songJson.getString("trackPrice"):"0";
//                        song.collectionPrice = songJson.getString("collectionPrice");
                        song.collectionPrice = (songJson.has("collectionPrice"))? songJson.getString("collectionPrice"):"0";
                        song.dateTime = (songJson.has("releaseDate"))? songJson.getString("releaseDate"):"N/A";
                        song.artworkUrl100 = (songJson.has("artworkUrl100"))? songJson.getString("artworkUrl100"):"N/A";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                        song.date = (songJson.has("releaseDate"))? sdf.parse(songJson.getString("releaseDate")): null;
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

            if(filter=="date")Collections.sort(result,Song.COMPARE_BY_DATE);
            else Collections.sort(result, Song.COMPARE_BY_PRICE);

            songs.clear();
            songs.addAll(result);
            if(songs.size()>0){
                mAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(mAdapter);
                enableAll();
            }
            else {
                Toast.makeText(MainActivity.this, "No Tracks Found", Toast.LENGTH_SHORT).show();
                enableAll();
            }

        }
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType()!=ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType()!=ConnectivityManager.TYPE_MOBILE)){
            return false;
        }
        return true;

    }

    private void disableAll(){
        pb.setVisibility(View.VISIBLE);
        editText.setVisibility(View.INVISIBLE);
        sb.setVisibility(View.INVISIBLE);
        buttonSearch.setVisibility(View.INVISIBLE);
        buttonReset.setVisibility(View.INVISIBLE);
        switchFilter.setVisibility(View.INVISIBLE);
        textViewSortBy.setVisibility(View.INVISIBLE);
        textViewResults.setVisibility(View.INVISIBLE);
        textViewDate.setVisibility(View.INVISIBLE);
        textViewLimit.setVisibility(View.INVISIBLE);
    }
    private void enableAll(){
        pb.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.VISIBLE);
        sb.setVisibility(View.VISIBLE);
        buttonSearch.setVisibility(View.VISIBLE);
        buttonReset.setVisibility(View.VISIBLE);
        switchFilter.setVisibility(View.VISIBLE);
        textViewSortBy.setVisibility(View.VISIBLE);
        textViewResults.setVisibility(View.VISIBLE);
        textViewDate.setVisibility(View.VISIBLE);
        textViewLimit.setVisibility(View.VISIBLE);
    }

}
