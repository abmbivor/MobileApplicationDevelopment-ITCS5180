package net.ddns.bivor.group14_ic10;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
    SignUpFragment.OnFragmentInteractionListener, NotesFragment.OnFragmentInteractionListener, AddNoteFragment.OnFragmentInteractionListener
, DisplayNoteFragment.OnFragmentInteractionListener{

    public static final ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "100";
        String highScore = sharedPref.getString("TOKEN", defaultValue);

        if(isConnected()){

            if(highScore.equals(defaultValue)){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new LoginFragment(), "tag_login").commit();
            }
            else {
               // new CheckAsyncTask().execute(highScore);

                OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall")
                    .header("x-access-token",""+highScore)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()){
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.container, new LoginFragment(), "tag_login").commit();
                        }
                        else {
                            notes.clear();

                            String jsonData = responseBody.string();
                            JSONObject jsonObject = new JSONObject(jsonData);
                            JSONArray jsonArray = jsonObject.getJSONArray("notes");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                Note note = new Note();

                                note.id = object.getString("_id");
                                note.userID = object.getString("userId");
                                note.text = object.getString("text");
                                note.__v = object.getInt("__v");

                                notes.add(note);
                            }

                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.container, new NotesFragment(), "tag_notes").commit();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        }
        else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }


//    private class CheckAsyncTask extends AsyncTask<Integer,Void,Boolean>{
//
//        @Override
//        protected Boolean doInBackground(Integer... integers) {
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//        }
//    }


    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType()!=ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType()!=ConnectivityManager.TYPE_MOBILE)){
            return false;
        }
        return true;

    }


    @Override
    public void goToSignupFromLogin() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SignUpFragment(), "tag_signup")
                .commit();
    }

    @Override
    public void goToNotesFromLogin() {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "100";
        String highScore = sharedPref.getString("TOKEN", defaultValue);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall")
                .header("x-access-token", highScore)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()){
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, new LoginFragment(), "tag_login").commit();
                    }
                    else {
                        notes.clear();

                        String jsonData = responseBody.string();
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray jsonArray = jsonObject.getJSONArray("notes");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            Note note = new Note();

                            note.id = object.getString("_id");
                            note.userID = object.getString("userId");
                            note.text = object.getString("text");
                            note.__v = object.getInt("__v");

                            notes.add(note);
                        }

                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new NotesFragment(), "tag_notes")
                                .commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void goToLoginFromSignUp() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment(), "tag_login")
                .commit();
    }

    @Override
    public void goToNotesFromSignUp() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new NotesFragment(), "tag_notes")
                .commit();
    }

    @Override
    public void goToAddNotesFromNotes() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddNoteFragment(), "tag_addNote")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToLoginFromNotes() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment(), "tag_login")
                .commit();
    }

    @Override
    public void goToMainAndComeBackToNotes() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "100";
        String highScore = sharedPref.getString("TOKEN", defaultValue);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall")
                .header("x-access-token", highScore)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()){
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, new LoginFragment(), "tag_login").commit();
                    }
                    else {
                        notes.clear();

                        String jsonData = responseBody.string();
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray jsonArray = jsonObject.getJSONArray("notes");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            Note note = new Note();

                            note.id = object.getString("_id");
                            note.userID = object.getString("userId");
                            note.text = object.getString("text");
                            note.__v = object.getInt("__v");

                            notes.add(note);
                        }
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new NotesFragment(), "tag_notes")
                                .commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void goToNotesFromAddNotes() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "100";
        String highScore = sharedPref.getString("TOKEN", defaultValue);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall")
                .header("x-access-token", highScore)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()){
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, new LoginFragment(), "tag_login").commit();
                    }
                    else {
                        notes.clear();

                        String jsonData = responseBody.string();
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray jsonArray = jsonObject.getJSONArray("notes");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            Note note = new Note();

                            note.id = object.getString("_id");
                            note.userID = object.getString("userId");
                            note.text = object.getString("text");
                            note.__v = object.getInt("__v");

                            notes.add(note);
                        }

                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new NotesFragment(), "tag_notes")
                                .commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void goToNotesFromDisplayNote() {

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new NotesFragment(), "tag_notes")
                .commit();
    }
}
