package net.ddns.bivor.group14_inclass12;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;


public class ShowTripFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private OnFragmentInteractionListener mListener;
    private static View rootView;

    Trip trip;

    TextView textViewDetailsTripName, textViewShowDate, textViewDetailsTripDestination;
    Button buttonClose;

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    public  static final int MY_PERMISSIONS_REQUEST = 1;

    public ShowTripFragment() {
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


        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_show_trip, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        textViewDetailsTripName = rootView.findViewById(R.id.textViewDetailsTripName);
        textViewShowDate = rootView.findViewById(R.id.textViewShowDate);
        textViewDetailsTripDestination = rootView.findViewById(R.id.textViewDetailsTripDestination);

        buttonClose = rootView.findViewById(R.id.buttonClose);

        textViewDetailsTripName.setText(trip.tripName);
        textViewShowDate.setText(trip.datePicked);
        textViewDetailsTripDestination.setText(trip.destination.name);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Trip Details");

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToTripsFromShowTrip();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("GPS Not Enabled")
                    .setMessage("Would you like to enable the GPS settings?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            getActivity().finish();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();

        } else {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("demo", location.getLatitude() + ", " + location.getLongitude());
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST );

                return;
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, locationListener);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, locationListener);

                } else {
                    Toast.makeText(getActivity(), "Storage Permission Not Given", Toast.LENGTH_SHORT).show();
                }
                return;
            }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final ArrayList<LatLng> coordList = new ArrayList<>();

        for(Place place: trip.places){
            coordList.add(new LatLng(Double.parseDouble(place.latitude),Double.parseDouble(place.longitude)));
        }

        for(int i=0;i<coordList.size();i++){
            LatLng marker = new LatLng(coordList.get(i).latitude, coordList.get(i).longitude);

            mMap.addMarker(new MarkerOptions()
                    .position(marker));
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback(){

            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for(int i=0;i<coordList.size();i++){
                    builder.include(coordList.get(i));
                }

                LatLngBounds bounds = builder.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            }
        });
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        void goToTripsFromShowTrip();

    }

}
