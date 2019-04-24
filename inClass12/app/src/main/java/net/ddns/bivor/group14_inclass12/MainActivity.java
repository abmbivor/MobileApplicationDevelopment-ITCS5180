package net.ddns.bivor.group14_inclass12;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements TripsFragment.OnFragmentInteractionListener, AddTripFragment.OnFragmentInteractionListener,
AddPlacesFragment.OnFragmentInteractionListener, ShowTripFragment.OnFragmentInteractionListener{

    public static final ArrayList<Trip> trips = new ArrayList<>();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference("trips");

        trips.clear();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                    Trip trip = expenseSnap.getValue(Trip.class);
                    trips.add(trip);
                }

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new TripsFragment(), "tag_trips").commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void goToTripFromAddPlaces() {

        mDatabase.setValue(trips);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new TripsFragment(), "tag_trips")
                .commit();
    }

    @Override
    public void goToAddTripFromAddPlaces() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddTripFragment(), "tag_addTrip")
                .commit();
    }

    @Override
    public void goToAddTripFromTrip() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddTripFragment(), "tag_addTrip")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToTripsFromShowTrip() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new TripsFragment(), "tag_trips")
                .commit();
    }
}
