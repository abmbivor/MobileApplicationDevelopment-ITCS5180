package net.ddns.bivor.inclass06;

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
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //StudentID: 801028228 ; Name: A B M Mohaimenur Rahman

    TextView textViewCategory,textViewDescription, textViewTitle, textViewPublishedAt, textViewNumber;
    Button buttonGo;
    ImageView iv, imageViewBack, imageViewNext;
    ProgressBar pb;
    LinearLayout descriptionLayout, titleLayout;
    LayoutInflater inflaterDescription, inflaterTitle;
    View viewDescription, viewTitle;
    String[] category = {"Business", "Entertainment","General", "Health", "Science", "Sports", "Technology"};
    ArrayList<Article> articles ;
    int currentSelectedIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        buttonGo = findViewById(R.id.buttonGo);

        textViewCategory = findViewById(R.id.textViewCategory);
        textViewPublishedAt = findViewById(R.id.textViewPublishedAt);
        textViewNumber = findViewById(R.id.textViewNumber);


        textViewPublishedAt.setVisibility(View.INVISIBLE);
        textViewNumber.setVisibility(View.INVISIBLE);


        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewNext = findViewById(R.id.imageViewNext);
        iv = findViewById(R.id.imageView);
        iv.setVisibility(View.INVISIBLE);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        imageViewBack.setEnabled(false);
        imageViewNext.setEnabled(false);

        articles = new ArrayList<>();


        descriptionLayout = findViewById(R.id.descriptionLayout);
        titleLayout = findViewById(R.id.titleLayout);

        inflaterDescription = LayoutInflater.from(this);
        inflaterTitle = LayoutInflater.from(this);

        viewDescription = inflaterDescription.inflate(R.layout.description,descriptionLayout,false);
        viewTitle = inflaterTitle.inflate(R.layout.title,titleLayout,false);

        textViewDescription = viewDescription.findViewById(R.id.textViewDescription);
        textViewTitle = viewTitle.findViewById(R.id.textViewTitle);

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Choose Category")
                            .setItems(category, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //keywordURLs.clear();
                                    //currentSelectedIndex=0;
                                    textViewCategory.setText(category[which]);
                                    pb.setVisibility(View.VISIBLE);
                                    textViewTitle.setText("Loading ...");
                                    //titleLayout.addView(viewTitle);

                                    new GetNewsAsync().execute(category[which]);
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
                iv.setVisibility(View.INVISIBLE);
                currentSelectedIndex-=1;
                if(currentSelectedIndex<0)currentSelectedIndex=articles.size()-1;

                textViewNumber.setText((currentSelectedIndex+1)+" out of " + articles.size());

                new getImageAsync().execute(currentSelectedIndex);
                //Picasso.get().load(articles.get(currentSelectedIndex).getUrlToImage()).into(iv);
                //pb.setVisibility(View.INVISIBLE);

                textViewDescription.setText(articles.get(currentSelectedIndex).getDescription());
               // descriptionLayout.addView(viewDescription);
                textViewTitle.setText(articles.get(currentSelectedIndex).getTitle());
                //titleLayout.addView(viewTitle);

                textViewPublishedAt.setText(articles.get(currentSelectedIndex).getPublishedAt());

            }
        });

        imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pb.setVisibility(View.VISIBLE);
                iv.setVisibility(View.INVISIBLE);
                currentSelectedIndex+=1;
                if(currentSelectedIndex>articles.size()-1)currentSelectedIndex=0;

                textViewNumber.setText((currentSelectedIndex+1)+" out of " + articles.size());

                new getImageAsync().execute(currentSelectedIndex);

                //Picasso.get().load(articles.get(currentSelectedIndex).getUrlToImage()).into(iv);
                //pb.setVisibility(View.INVISIBLE);

                textViewDescription.setText(articles.get(currentSelectedIndex).getDescription());
                //descriptionLayout.addView(viewDescription);
                textViewTitle.setText(articles.get(currentSelectedIndex).getTitle());
                //titleLayout.addView(viewTitle);

                textViewPublishedAt.setText(articles.get(currentSelectedIndex).getPublishedAt());

            }
        });
    }

    private class GetNewsAsync extends AsyncTask<String, Void, ArrayList<Article>> {
        @Override
        protected ArrayList<Article> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Article> result = new ArrayList<>();

            try {
                URL url = new URL("https://newsapi.org/v2/top-headlines?country=us&category="+ params[0]+
                        "&apiKey=7eea134a683d4402ab4eae8bc46af0d2");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray articles = root.getJSONArray("articles");
                    for (int i=0;i<articles.length();i++) {
                        JSONObject articleJson = articles.getJSONObject(i);
                        Article article = new Article();
                        article.title = articleJson.getString("title");
                        article.publishedAt = articleJson.getString("publishedAt");
                        article.urlToImage = articleJson.getString("urlToImage");
                        article.description = articleJson.getString("description");

                        result.add(article);
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
        protected void onPostExecute(ArrayList<Article> result) {

            articles.clear();
            articles.addAll(result);
            currentSelectedIndex = 0;


            if(articles.size()>0){
                descriptionLayout.removeAllViews();
                titleLayout.removeAllViews();

                textViewNumber.setVisibility(View.VISIBLE);
                textViewNumber.setText(1+" out of " + result.size());

                new getImageAsync().execute(currentSelectedIndex);
                //pb.setVisibility(View.INVISIBLE);
                //Picasso.get().load(articles.get(0).getUrlToImage()).into(iv);

                //Log.d("demo" , "imageURL : "+articles.get(0).getUrlToImage());
                textViewDescription.setText(articles.get(0).getDescription());
                descriptionLayout.addView(viewDescription);

                textViewTitle.setText(articles.get(0).getTitle());
                titleLayout.addView(viewTitle);

                textViewPublishedAt.setText(articles.get(0).getPublishedAt());

                textViewPublishedAt.setVisibility(View.VISIBLE);

                imageViewBack.setEnabled(true);
                imageViewNext.setEnabled(true);
            }
            else {
                Toast.makeText(MainActivity.this, "No News Found", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private class getImageAsync extends AsyncTask<Integer,Void, Bitmap>{
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            pb.setVisibility(View.INVISIBLE);
            iv.setImageBitmap(bitmap);
            iv.setVisibility(View.VISIBLE);
            if(articles.size()>1) {
                imageViewBack.setEnabled(true);
                imageViewNext.setEnabled(true);
            }
        }

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            try {
                URL url = new URL(articles.get(integers[0]).getUrlToImage());
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
