package net.ddns.bivor.inclass09_group14;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
SignUpFragment.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener,
CreateContactFragment.OnFragmentInteractionListener{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public static final ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            mDatabase = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("contacts");
            contacts.clear();

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                        Contact contact = expenseSnap.getValue(Contact.class);
                        contacts.add(contact);
                    }
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new ContactFragment(), "tag_contact").commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment(), "tag_contact").commit();
        }
        
    }


    @Override
    public void goToCreateNewContact() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CreateContactFragment(), "tag_signUp")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToLogInFromContact() {
        contacts.clear();
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment(), "tag_log")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToContactFromCreateNewContact(Contact contact) {

        contacts.add(contact);
        mDatabase = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
        mDatabase.child("contacts").setValue(contacts);


        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ContactFragment(), "tag_contact")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToSignUp() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SignUpFragment(), "tag_signUp")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToContactFromLogIn() {
        mDatabase = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("contacts");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                    Contact contact = expenseSnap.getValue(Contact.class);
                    contacts.add(contact);
                }
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ContactFragment(), "tag_contact")
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void goToLogInFromSignUp() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment(), "tag_login")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToContactFromSignUp() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ContactFragment(), "tag_contact")
                .addToBackStack(null)
                .commit();
    }
}
