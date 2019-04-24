package net.ddns.bivor.group14_inclass12;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class AddPlacesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private OnFragmentInteractionListener mListener;

    Trip trip;

    TextView textViewDestinationCity, textViewPlaceType;
    Button buttonSearchPlaces, buttonSave, buttonCancel;

    RecyclerView recyclerViewPlaces;
    RecyclerView.Adapter mAdapter;

    ArrayList<String> keywords = new ArrayList<>();
    public static final ArrayList<Place> places = new ArrayList<>();
    public static final ArrayList<Place> selectedPlaces = new ArrayList<>();

    public AddPlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trip = (Trip) getArguments().getSerializable("TRIP_KEY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_places, container, false);

        keywords.add("airport");keywords.add("amusement parks");keywords.add("aquarium");keywords.add("car rental");
        keywords.add("museum");keywords.add("police station");keywords.add("city hall");keywords.add("parking");

        textViewDestinationCity = rootView.findViewById(R.id.textViewDestinationCity);
        textViewPlaceType = rootView.findViewById(R.id.textViewPlaceType);

        buttonSearchPlaces = rootView.findViewById(R.id.buttonSearchPlaces);
        buttonSave = rootView.findViewById(R.id.buttonSave);
        buttonCancel = rootView.findViewById(R.id.buttonCancel);

        recyclerViewPlaces = (RecyclerView) rootView.findViewById(R.id.recyclerViewPlaces);

        // 2. set layoutManger
        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));

        places.clear();

        mAdapter = new PlacesAdapter(places, communication);
        recyclerViewPlaces.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Adding Places");

        textViewDestinationCity.setText(trip.destination.name);

        textViewPlaceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Choose a Keyword")
                        .setItems(keywords.toArray(new CharSequence[keywords.size()]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textViewPlaceType.setText(keywords.get(which));
                            }
                        });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToAddTripFromAddPlaces();
            }
        });


        buttonSearchPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                places.clear();
                mAdapter.notifyDataSetChanged();

                textViewPlaceType.setEnabled(false);
                buttonSave.setEnabled(false);
                buttonCancel.setEnabled(false);
                buttonSearchPlaces.setEnabled(false);
                if(textViewPlaceType.getText().toString().equals("Type of Place")){
                    Toast.makeText(getActivity(), "Select a type of place!", Toast.LENGTH_SHORT).show();
                    textViewPlaceType.setEnabled(true);
                    buttonSave.setEnabled(true);
                    buttonCancel.setEnabled(true);
                    buttonSearchPlaces.setEnabled(true);
                }
                else {
                    OkHttpClient client = new OkHttpClient();

                    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                            +trip.destination.latitude+","+trip.destination.longitude+
                            "&radius=15000&type="+textViewPlaceType.getText().toString().trim()+"&key=AIzaSyCMF1dCvLKh3UhOUt8asGklGM-JqxVgDuY";

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            Toast.makeText(getActivity(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful()){
                                    Toast.makeText(getActivity(), "Response is not successful", Toast.LENGTH_SHORT).show();

                                }
                                else {

                                    String jsonData = responseBody.string();
                                    JSONObject jsonObject = new JSONObject(jsonData);
                                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        Place place = new Place();

                                        place.name = object.getString("name");

                                        JSONObject geometry = object.getJSONObject("geometry");
                                        JSONObject location = geometry.getJSONObject("location");

                                        place.latitude = ""+location.getDouble("lat");
                                        place.longitude = ""+location.getDouble("lng");

                                        places.add(place);

                                    }



                                    if(getActivity()!=null){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(places.size()==0){
                                                    Toast.makeText(getActivity(), "No Results Found", Toast.LENGTH_SHORT).show();
                                                }
                                                mAdapter.notifyDataSetChanged();
                                                textViewPlaceType.setEnabled(true);
                                                buttonSave.setEnabled(true);
                                                buttonCancel.setEnabled(true);
                                                buttonSearchPlaces.setEnabled(true);
                                                return;
                                            }
                                        });
                                    }



                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trip.places.addAll(selectedPlaces);
                MainActivity.trips.add(trip);
                selectedPlaces.clear();
                mListener.goToTripFromAddPlaces();
            }
        });


    }

    FragmentCommunication communication=new FragmentCommunication(){

        @Override
        public void goToShowTripfromTrip(Trip trip) {
            
        }

        @Override
        public void respond(Place place, int index) {
            if(selectedPlaces.size()<=15){
                selectedPlaces.add(place);
                places.remove(place);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Place Added to the TRIP", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), "Can't add more places. MAX LIMIT 15 REACHED!", Toast.LENGTH_SHORT).show();
            }
            
        }

        @Override
        public void goToAddPlacesFromAddTrip() {

        }
    };

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToTripFromAddPlaces();
        void goToAddTripFromAddPlaces();

    }

}
