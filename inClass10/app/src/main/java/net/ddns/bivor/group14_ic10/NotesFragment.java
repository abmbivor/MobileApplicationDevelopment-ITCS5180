package net.ddns.bivor.group14_ic10;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NotesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    Note note;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;

    TextView textViewNoteUserName;
    ImageView imageViewSignOut;
    Button buttonAddNote;

    String userName="";

    String highScore;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_notes, container, false);

// 1. get a reference to recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // 2. set layoutManger
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setRecycleChildrenOnDetach(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new NoteAdapter(MainActivity.notes, communication);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        note = new Note();

        textViewNoteUserName = rootView.findViewById(R.id.textViewNoteUserName);
        imageViewSignOut = rootView.findViewById(R.id.imageViewSignOut);
        buttonAddNote = rootView.findViewById(R.id.buttonAddNote);

        textViewNoteUserName.setText("");




        return  rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Notes");

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "100";
        highScore = sharedPref.getString("TOKEN", defaultValue);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/me")
                .header("x-access-token", highScore)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {

                    } else {
                        String jsonData = responseBody.string();
                        JSONObject jsonObject = new JSONObject(jsonData);

                        userName = jsonObject.getString("name");
//                        textViewNoteUserName.setText("Hey " + jsonObject.getString("name") + "!!!");

                        if(getActivity()!=null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewNoteUserName.setText("Hey " + userName + "!!!");
                                   // return;
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToAddNotesFromNotes();
            }
        });

        imageViewSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("TOKEN", "100");
                editor.apply();

                mListener.goToLoginFromNotes();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

   FragmentCommunication communication = new FragmentCommunication() {
       @Override
       public void goToDisplayNote(Note note) {
           DisplayNoteFragment displayNoteFragment=new DisplayNoteFragment();
           Bundle bundle=new Bundle();
           bundle.putSerializable("NOTE",note);
           displayNoteFragment.setArguments(bundle);
           FragmentManager manager=getFragmentManager();
           FragmentTransaction transaction=manager.beginTransaction();
           transaction.replace(R.id.container, displayNoteFragment)
                   .addToBackStack(null).commit();
       }

       @Override
       public void deleteNote(Note note, int index) {

           MainActivity.notes.clear();

           OkHttpClient client = new OkHttpClient();

           Request request = new Request.Builder()
                   .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/delete?msgId="+note.id)
                   .header("x-access-token", highScore)
                   .addHeader("Content-Type", "application/x-www-form-urlencoded")
                   .build();

           client.newCall(request).enqueue(new Callback() {
               @Override
               public void onFailure(Call call, IOException e) {
                   Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                   e.printStackTrace();
               }

               @Override
               public void onResponse(Call call, Response response) throws IOException {
                   try (ResponseBody responseBody = response.body()) {
                       if (!response.isSuccessful()) {

                       } else {
                           mListener.goToMainAndComeBackToNotes();
                       }
                   }
               }
           });


       }
   };

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
      void goToAddNotesFromNotes();
      void goToLoginFromNotes();
      void goToMainAndComeBackToNotes();
    }
}
