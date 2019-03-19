package net.ddns.bivor.group14_hw02;

import android.app.ProgressDialog;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.util.*;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    TextView textViewCountThread, textViewCountAsync, textViewLengthThread, textViewLengthAsync, textViewPB;
    SeekBar sbCountThread, sbLengthThread, sbCountAsync, sbLengthAsync;
    Button buttonGenerate;
    ProgressDialog pd;
    ProgressBar pb;
    Handler handler;
    ExecutorService threadPool;
    int countThread=1, countAsync=1, lengthThread=7, lengthAsync=7, progressValue=0;
    int FLAG_THREAD =0, FLAG_ASYNC = 0;

    ProgressTracker pT;

    ArrayList<String> arrayListPasswordThread;
    ArrayList<String> arrayListPasswordAsync;

    static final String PASSWORD_THREAD ="PASSWORD_THREAD";
    static final String PASSWORD_ASYNC  = "PASSWORD_ASYNC";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Password Generator");

        pT = new ProgressTracker(0);

        textViewCountThread = findViewById(R.id.textViewCountThread);
        textViewLengthThread = findViewById(R.id.textViewLengthThread);
        textViewCountAsync = findViewById(R.id.textViewCountAsync);
        textViewLengthAsync = findViewById(R.id.textViewLengthAsync);
        textViewPB = findViewById(R.id.textViewPB);

        sbCountThread = findViewById(R.id.seekBarCountThread);
        sbLengthThread = findViewById(R.id.seekBarLengthThread);
        sbCountAsync = findViewById(R.id.seekBarCountAsync);
        sbLengthAsync = findViewById(R.id.seekBarLengthAsync);

        buttonGenerate = findViewById(R.id.buttonGenerate);

        pd = new ProgressDialog(this);
        pd.setMessage("Generating Passwords");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);

        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        textViewPB.setVisibility(View.INVISIBLE);



        intent = new Intent(MainActivity.this,GeneratedPasswordsActivity.class);

        threadPool = Executors.newFixedThreadPool(2);


        sbCountThread.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textViewCountThread.setText(""+(progress+1));
                    countThread = progress+1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbLengthThread.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textViewLengthThread.setText(""+(progress+7));
                    lengthThread = progress+7;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbCountAsync.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textViewCountAsync.setText(""+(progress+1));
                    countAsync = progress+1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbLengthAsync.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textViewLengthAsync.setText(""+(progress+7));
                    lengthAsync = progress+7;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Generating Started", Toast.LENGTH_SHORT).show();

                pb.setVisibility(View.VISIBLE);
                textViewPB.setVisibility(View.VISIBLE);

                pd.setMax(countThread+countAsync);
                pd.setProgress(0);

                pb.setMax(countThread+countAsync);
                pb.setProgress(0);

                visibilityItems(View.INVISIBLE);

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                handler =new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        switch (msg.what){

                            case DoWork.STATUS_START:
                                //pd.show();
                                break;

                            case DoWork.STATUS_STOP:
                                Passwords passwordThread = new Passwords(arrayListPasswordThread);
                                intent.putExtra(PASSWORD_THREAD,passwordThread);

                                FLAG_THREAD =1;
                                
                                if (FLAG_ASYNC==1){
                                    //pd.dismiss();
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    startActivity(intent);
                                    finish();
                                }

                                break;

                            case DoWork.STATUS_PROGRESS:
                                synchronized (pT) {
                                    pT.setProgressVal(pT.getProgressVal()+1);
                                    //progressValue += 1;
                                    pb.setProgress(pT.getProgressVal());
                                    textViewPB.setText("" + (pT.getProgressVal() * 100) / (countThread + countAsync) + " %");
                                    //pd.setProgress(progressValue);
                                }
                                break;
                        }
                        return false;
                    }
                });

                threadPool.execute(new DoWork());
                new DoWorkAsync().execute(countAsync, lengthAsync);

            }
        });

    }

    public class ProgressTracker {

        int progressVal=0;

        public ProgressTracker(int progressVal) {
            this.progressVal = progressVal;
        }

        public int getProgressVal() {
            return progressVal;
        }

        public void setProgressVal(int progressVal) {
            this.progressVal = progressVal;
        }
    }


    public void visibilityItems(int a){

        findViewById(R.id.textView).setVisibility(a);
        findViewById(R.id.textView2).setVisibility(a);
        findViewById(R.id.textView4).setVisibility(a);
        findViewById(R.id.textView6).setVisibility(a);
        findViewById(R.id.textView7).setVisibility(a);
        findViewById(R.id.textView9).setVisibility(a);
        findViewById(R.id.textView).setVisibility(a);
        textViewCountThread.setVisibility(a);
        textViewLengthThread.setVisibility(a);
        textViewCountAsync.setVisibility(a);
        textViewLengthAsync.setVisibility(a);
        sbCountThread.setVisibility(a);
        sbLengthThread.setVisibility(a);
        sbCountAsync.setVisibility(a);
        sbLengthAsync.setVisibility(a);
    }

    class DoWork implements Runnable{

        static final int STATUS_START = 0x00;
        static final int STATUS_PROGRESS = 0x01;
        static final int STATUS_STOP = 0x02;

        @Override
        public void run() {

            arrayListPasswordThread = new ArrayList<>(countThread);

            Message startMessage = new Message();
            startMessage.what = STATUS_START;
            handler.sendMessage(startMessage);

            for (int i=0; i<countThread; i++){
//                Util generator = new Util();
                String password = Util.getPassword(lengthThread);
                arrayListPasswordThread.add(password);
                Message message = new Message();
                message.what = STATUS_PROGRESS;
                handler.sendMessage(message);
            }

            Message stopMessage = new Message();
            stopMessage.what = STATUS_STOP;
            handler.sendMessage(stopMessage);

        }
    }

    class DoWorkAsync extends AsyncTask<Integer, Integer, ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            Passwords passwordAsync = new Passwords(arrayListPasswordAsync);
            intent.putExtra(PASSWORD_ASYNC,passwordAsync);

            FLAG_ASYNC =1;

            if (FLAG_THREAD==1){
                //pd.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                startActivity(intent);
                finish();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            synchronized (pT){
                pT.setProgressVal(pT.getProgressVal()+values[0]);
                //progressValue+=values[0];
                //pd.setProgress(progressValue);
                pb.setProgress(pT.getProgressVal());
                textViewPB.setText(""+(pT.getProgressVal()*100)/(countThread+countAsync)+" %");
            }
        }

        @Override
        protected ArrayList<String> doInBackground(Integer... integers) {
            int count = integers[0];
            int length = integers[1];

            arrayListPasswordAsync = new ArrayList<>(count);

            for (int i=0; i<count; i++){
                arrayListPasswordAsync.add(Util.getPassword(length));
                publishProgress(1);
            }

            return arrayListPasswordAsync;
        }
    }
}
