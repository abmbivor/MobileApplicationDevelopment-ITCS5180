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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TripsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerViewTrips;
    RecyclerView.Adapter mAdapter;
    Button buttonAddTrip;

    public TripsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_trips, container, false);

        buttonAddTrip = rootView.findViewById(R.id.buttonAddNewTrip);


        recyclerViewTrips = (RecyclerView) rootView.findViewById(R.id.recyclerViewTrips);

        // 2. set layoutManger
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new TripsAdapter(MainActivity.trips, communication);
        recyclerViewTrips.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Trips");

        if(MainActivity.trips.size()>0)getActivity().findViewById(R.id.textViewTripMessage).setVisibility(View.INVISIBLE);


        buttonAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToAddTripFromTrip();
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

    FragmentCommunication communication=new FragmentCommunication(){

        @Override
        public void goToShowTripfromTrip(Trip trip) {
            ShowTripFragment showTrip =new ShowTripFragment();
            Bundle bundle=new Bundle();
            bundle.putSerializable("TRIP_KEY",trip);
            showTrip.setArguments(bundle);
            FragmentManager manager=getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.container,showTrip)
                    .addToBackStack(null).commit();
        }

        @Override
        public void respond(Place place, int index) {

        }

        @Override
        public void goToAddPlacesFromAddTrip() {

        }
    };

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToAddTripFromTrip();

    }


}
