package net.ddns.bivor.group14_hw03;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class Display extends AppCompatActivity {

    Button buttonFinish;
    ImageView iv;
    ProgressBar pb;
    TextView textViewDisplayTrack, textViewDisplayGenre, textViewDisplayArtist, textViewDisplayAlbum, textViewDisplayTrackPrice,
            textViewDisplayAlbumPrice;
    Song song;
    ArrayList<Song> songList;
//    String keyword, state;
//    int progressValue;

    static final String SONG_KEY2 = "SONG_KEY2";
    static final String SONGS_KEY2 = "SONGS_KEY2";
    static final String KEYWORD2 = "KEYWORD2";
    static final String STATE2 = "STATE2";
    static final String PROGRESS_VALUE2 = "PROGRESS_VALUE2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        setTitle("iTunes Music Search");

        buttonFinish = findViewById(R.id.buttonFinish);
        iv = findViewById(R.id.imageView);
//        iv.setAdjustViewBounds(true);
//        iv.setMaxHeight(100);
//        iv.setMaxWidth(100);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        textViewDisplayTrack = findViewById(R.id.textViewDisplayTrack);
        textViewDisplayGenre = findViewById(R.id.textViewDisplayGenre);
        textViewDisplayArtist = findViewById(R.id.textViewDisplayArtist);
        textViewDisplayAlbum = findViewById(R.id.textViewDisplayAlbum);
        textViewDisplayTrackPrice = findViewById(R.id.textViewDisplayTrackPrice);
        textViewDisplayAlbumPrice = findViewById(R.id.textViewDisplayAlbumPrice);

        pb = findViewById(R.id.progressBarDisplay);
        pb.setVisibility(View.INVISIBLE);

        songList = new ArrayList<>();

        if(getIntent()!=null && getIntent().getExtras() != null) {
            song = (Song) getIntent().getExtras().getSerializable(SongAdapter.SONG_KEY);
            songList = (ArrayList<Song>) getIntent().getExtras().getSerializable(SongAdapter.SONGS_KEY);
//            keyword = getIntent().getExtras().getString(SongAdapter.KEYWORD);
//            state = getIntent().getExtras().getString(SongAdapter.STATE);
//            progressValue = getIntent().getExtras().getInt(SongAdapter.PROGRESS_VALUE);
            disableAll();
            new getImageAsync().execute(song.artworkUrl100);
        }


        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Display.this, MainActivity.class);
                intent.putExtra(SONG_KEY2,song);
                intent.putExtra(SONGS_KEY2,songList);
//                intent.putExtra(KEYWORD2,keyword);
//                intent.putExtra(STATE2,state);
//                intent.putExtra(PROGRESS_VALUE2,progressValue);
                startActivity(intent);
                finish();
            }
        });
    }

    private class getImageAsync extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iv.setImageBitmap(bitmap);
            textViewDisplayTrack.setText("Track: "+song.trackName);
            textViewDisplayGenre.setText("Genre: "+song.primaryGenreName);
            textViewDisplayArtist.setText("Artist: "+song.artistName);
            textViewDisplayAlbum.setText("Album: "+song.collectionName);
            textViewDisplayTrackPrice.setText("Track Price: "+song.trackPrice);
            textViewDisplayAlbumPrice.setText("Album Price: "+song.collectionPrice);
            enableAll();
        }

        @Override
        protected Bitmap doInBackground(String... Strings) {
            try {
                URL url = new URL(Strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private void disableAll(){
        pb.setVisibility(VISIBLE);
        iv.setVisibility(View.INVISIBLE);
        buttonFinish.setVisibility(View.INVISIBLE);
        textViewDisplayTrack.setVisibility(View.INVISIBLE);
        textViewDisplayGenre.setVisibility(View.INVISIBLE);
        textViewDisplayArtist.setVisibility(View.INVISIBLE);
        textViewDisplayAlbum.setVisibility(View.INVISIBLE);
        textViewDisplayTrackPrice.setVisibility(View.INVISIBLE);
        textViewDisplayAlbumPrice.setVisibility(View.INVISIBLE);
    }
    private void enableAll(){
        pb.setVisibility(View.INVISIBLE);
        iv.setVisibility(View.VISIBLE);
        buttonFinish.setVisibility(VISIBLE);
        textViewDisplayTrack.setVisibility(VISIBLE);
        textViewDisplayGenre.setVisibility(VISIBLE);
        textViewDisplayArtist.setVisibility(VISIBLE);
        textViewDisplayAlbum.setVisibility(VISIBLE);
        textViewDisplayTrackPrice.setVisibility(VISIBLE);
        textViewDisplayAlbumPrice.setVisibility(VISIBLE);
    }
}
