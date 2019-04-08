package net.ddns.bivor.group14_hw06;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
SignUpFragment.OnFragmentInteractionListener, ChatRoomFragment.OnFragmentInteractionListener{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public static final ArrayList<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(isConnected()){
            if(mAuth.getCurrentUser()!=null){
//            mDatabase = FirebaseDatabase.getInstance().getReference("messages");
//
//
//            mDatabase.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    messages.clear();
//                    for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
//                        Message message = expenseSnap.getValue(Message.class);
//                        messages.add(message);
//                    }
//                    getSupportFragmentManager().beginTransaction()
//                            .add(R.id.container, new ChatRoomFragment(messages), "tag_chat").commit();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(getApplicationContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
//                }
//            });

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new ChatRoomFragment(messages), "tag_chat").commit();

            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new LoginFragment(), "tag_login").commit();
            }
        }
        else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void goToLoginFromChat() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment(), "tag_login")
//                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToMainAndComeBackToChat(Message message) {
        mDatabase = FirebaseDatabase.getInstance().getReference("messages");
        messages.clear();
        final Message newMessage = message;

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                    Message message = expenseSnap.getValue(Message.class);
                    messages.add(message);
                }
                messages.add(newMessage);
                mDatabase = FirebaseDatabase.getInstance().getReference("messages");
                mDatabase.setValue(messages);

                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ChatRoomFragment(messages), "tag_chat")
                        .commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void goToSignupFromLogin() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SignUpFragment(), "tag_signup")
//                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToChatFromLogin() {
//        mDatabase = FirebaseDatabase.getInstance().getReference("messages");
//
//
//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                messages.clear();
//                for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
//                    Message message = expenseSnap.getValue(Message.class);
//                    messages.add(message);
//                }
//                getSupportFragmentManager().popBackStack();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, new ChatRoomFragment(messages), "tag_chat")
//                        .commit();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ChatRoomFragment(messages), "tag_chat")
                .commit();

    }

    @Override
    public void goToLoginFromSignup() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment(), "tag_login")
//                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToChatFromSignup() {
//        mDatabase = FirebaseDatabase.getInstance().getReference("messages");
//
//
//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                messages.clear();
//                for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
//                    Message message = expenseSnap.getValue(Message.class);
//                    messages.add(message);
//                }
//                getSupportFragmentManager().popBackStack();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, new ChatRoomFragment(messages), "tag_chat")
////                        .addToBackStack(null)
//                        .commit();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ChatRoomFragment(messages), "tag_chat")
//                        .addToBackStack(null)
                .commit();


    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }
        else super.onBackPressed();
    }

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
}
