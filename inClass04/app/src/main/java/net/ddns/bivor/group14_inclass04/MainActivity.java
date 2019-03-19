package net.ddns.bivor.group14_inclass04;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    ImageView iv;
    ProgressBar pb;
    Button buttonAsync, buttonThread;
    ExecutorService threadPool;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Display Image");

        iv = findViewById(R.id.imageView);
        pb = findViewById(R.id.progressBar);
        buttonAsync = findViewById(R.id.buttonAsync);
        buttonThread = findViewById(R.id.buttonThread);
        threadPool = Executors.newFixedThreadPool(1);

        buttonAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iv.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);

                new DoWorkAsync().execute("https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg");


            }
        });

        buttonThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);

                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {

                        pb.setVisibility(View.INVISIBLE);
                        iv.setImageBitmap((Bitmap)msg.obj);
                        iv.setVisibility(View.VISIBLE);
                        return false;
                    }
                });

                threadPool.execute(new DoWork());

            }
        });


    }

    class DoWork implements Runnable{

        @Override
        public void run() {

            Bitmap bitmap = getImageBitmap("https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg");

            Message msg = new Message();
            msg.obj = bitmap;
            handler.sendMessage(msg);

        }

        Bitmap getImageBitmap(String string) {
            try {
                URL url = new URL(string);
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

    class DoWorkAsync extends AsyncTask<String, Integer, Bitmap>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            pb.setVisibility(View.INVISIBLE);
            iv.setImageBitmap(bitmap);
            iv.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
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
}
