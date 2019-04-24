package net.ddns.bivor.group14_inclass12;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class AddTripFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final int REQUEST_CODE = 10;

    private OnFragmentInteractionListener mListener;

    Button buttonPickDate, buttonSearchDestination;
    TextView textViewDate, textViewDestinationResult;
    EditText editTextTripName, editTextDestinationCity;

    Trip trip;

    public AddTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_trip, container, false);

        buttonPickDate  = rootView.findViewById(R.id.buttonPickDate);
        buttonSearchDestination = rootView.findViewById(R.id.buttonSearchDestination);

        textViewDate = rootView.findViewById(R.id.textViewDate);
        textViewDestinationResult = rootView.findViewById(R.id.textViewDestinationResult);

        editTextTripName = rootView.findViewById(R.id.editTextTripName);
        editTextDestinationCity = rootView.findViewById(R.id.editTextDestinationCity);

        trip = new Trip();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Adding a Trip");

        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddTripFragment.DatePickerFragment();
                newFragment.setTargetFragment(AddTripFragment.this, REQUEST_CODE);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        buttonSearchDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextTripName.getText().toString().equals("")){
                    editTextTripName.setError("Enter a trip name!");
                }
                else if (editTextDestinationCity.getText().toString().equals("")){
                    editTextDestinationCity.setError("Enter a Destination City Name");
                }
                else if(textViewDate.getText().equals("Select a Date")){
                    Toast.makeText(getActivity(), "Select a Date", Toast.LENGTH_SHORT).show();
                }
                else {

                    trip.tripName = editTextTripName.getText().toString().trim();

                    OkHttpClient client = new OkHttpClient();

                    String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+editTextDestinationCity.getText().toString().trim() +
                            "&inputtype=textquery&fields=formatted_address,name," +
                            "geometry&key=AIzaSyCMF1dCvLKh3UhOUt8asGklGM-JqxVgDuY&type=city_hall";

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
                                    JSONArray jsonArray = jsonObject.getJSONArray("candidates");

                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        JSONObject geometry = object.getJSONObject("geometry");
                                        JSONObject location = geometry.getJSONObject("location");

                                        trip.destination.name = object.getString("formatted_address");
                                        trip.destination.latitude = ""+location.getDouble("lat");
                                        trip.destination.longitude = ""+location.getDouble("lng");

                                        if(getActivity()!=null){
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    textViewDestinationResult.setText(trip.destination.name);
                                                    textViewDestinationResult.setVisibility(View.VISIBLE);
                                                    return;
                                                }
                                            });
                                        }

                                    }



                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    textViewDestinationResult.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(editTextTripName.getText().toString().equals("")){
                                editTextTripName.setError("Enter a Trip Name");
                            }
                            else if(textViewDate.getText().toString().equals("Select a Date")){
                                Toast.makeText(getActivity(), "Select a Date", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                AddPlacesFragment addPlacesFragment =new AddPlacesFragment();
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("TRIP_KEY",trip);
                                addPlacesFragment.setArguments(bundle);
                                FragmentManager manager=getFragmentManager();
                                FragmentTransaction transaction=manager.beginTransaction();
                                transaction.replace(R.id.container,addPlacesFragment)
                                        .addToBackStack(null).commit();
                            }

                        }
                    });
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
//            String dateExtra = data.getStringExtra("pickedDate");
//            Date date1 = null;
//            try {
//                date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateExtra);
//                trip.date = date1;
//                Log.d("demo", date1.toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


            String date = data.getStringExtra("selectedDate");

            textViewDate.setText(date);
            trip.datePicked = date;
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name


    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String date = ""+MONTHS[month]+" "+day+", "+year;
            String datePicked = day+"/"+(month+1)+"/"+year;
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_OK,
                    new Intent().putExtra("selectedDate", date).putExtra("pickedDate", datePicked)
            );
        }
    }

}
