package net.ddns.bivor.group14_inclass04;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonGo;
    TextView textViewSearch;
    ImageView imageViewBack, imageViewNext, imageViewGallery;
    ArrayList<String> keywords, keywordURLs;
    ProgressBar pb;
    Integer currentSelectedIndex=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");
        buttonGo = findViewById(R.id.buttonGo);
        textViewSearch  = findViewById(R.id.textViewSearch);
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewNext = findViewById(R.id.imageViewNext);
        imageViewGallery = findViewById(R.id.imageViewGallery);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        imageViewBack.setEnabled(false);
        imageViewNext.setEnabled(false);

        keywords = new ArrayList();
        keywordURLs = new ArrayList();

        new getKeywordAsync().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
        
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Choose a Keyword")
                            .setItems(keywords.toArray(new CharSequence[keywords.size()]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //keywordURLs.clear();
                                    //currentSelectedIndex=0;
                                    textViewSearch.setText(keywords.get(which));
                                    new getURLAsync().execute(keywords.get(which));
                                    imageViewGallery.setVisibility(View.INVISIBLE);
                                    pb.setVisibility(View.VISIBLE);
                                }
                            });
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    Toast.makeText(MainActivity.this, "There is no internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                imageViewGallery.setVisibility(View.INVISIBLE);
                currentSelectedIndex-=1;
                if(currentSelectedIndex<0)currentSelectedIndex=keywordURLs.size()-1;

                new getImageAsync().execute(currentSelectedIndex);


            }
        });

        imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                imageViewGallery.setVisibility(View.INVISIBLE);
                currentSelectedIndex+=1;
                if(currentSelectedIndex>keywordURLs.size()-1)currentSelectedIndex=0;

                new getImageAsync().execute(currentSelectedIndex);
            }
        });



    }

    private class getImageAsync extends AsyncTask<Integer,Void, Bitmap>{
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            pb.setVisibility(View.INVISIBLE);
            imageViewGallery.setImageBitmap(bitmap);
            imageViewGallery.setVisibility(View.VISIBLE);
            if(keywordURLs.size()>1) {
                imageViewBack.setEnabled(true);
                imageViewNext.setEnabled(true);
            }
        }

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            try {
                URL url = new URL(keywordURLs.get(integers[0]));
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

    private class getURLAsync extends AsyncTask<String,Void,ArrayList<String>>{
        @Override
        protected void onPostExecute(ArrayList<String> newList) {
            keywordURLs.clear();
            keywordURLs.addAll(newList);
            currentSelectedIndex = 0;

            if(keywordURLs.isEmpty()){
                    imageViewGallery.setImageResource(R.drawable.ic_launcher_foreground);
                    imageViewGallery.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.INVISIBLE);
                    imageViewBack.setEnabled(false);
                    imageViewNext.setEnabled(false);
                    Toast.makeText(MainActivity.this, "No Image Found", Toast.LENGTH_SHORT).show();
                }
                else{
                    new getImageAsync().execute(currentSelectedIndex);
                }

        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            ArrayList<String> urls = new ArrayList<>();
            String result = null;
            try {
                URL url = new URL(" http://dev.theappsdr.com/apis/photos/index.php?keyword="+strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if(!result.trim().equals("")){
                        urls.addAll(Arrays.asList(result.split("\n")));
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(connection !=null)connection.disconnect();
            }

            return urls;
        }
    }

    private class getKeywordAsync extends AsyncTask<String,Void, ArrayList<String>>{

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            keywords.clear();
            keywords.addAll(strings);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            HttpURLConnection connection = null;
            String result = null;
            ArrayList<String> keys = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF8");
                    keys.addAll( Arrays.asList(result.split(";")));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(connection !=null)connection.disconnect();
            }
            return keys;
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
}
