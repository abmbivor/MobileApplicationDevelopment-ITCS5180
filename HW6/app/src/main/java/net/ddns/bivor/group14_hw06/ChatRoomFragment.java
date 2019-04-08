package net.ddns.bivor.group14_hw06;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatRoomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChatRoomFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Message message;

    ArrayList<Message> messages;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;

    TextView textViewChatName;
    EditText editTextChatMessage;
    ImageView imageViewSendMessage, imageViewAddImage, imageViewLogout;

    private int PICK_IMAGE_REQUEST = 1;
    public  static final int MY_PERMISSIONS_REQUEST = 11;

    int FLAG = 0;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mDatabase2;

    private ArrayList<String> currentUser;      // keeping the info of current user. 0 - firstName 1- lastName 2- Email 3- Password

    public ChatRoomFragment() {
    }

    public ChatRoomFragment(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);

        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // 2. set layoutManger
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setRecycleChildrenOnDetach(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ChatRoomAdapter(MainActivity.messages, communication);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        textViewChatName = rootView.findViewById(R.id.textViewChatName);
        editTextChatMessage = rootView.findViewById(R.id.editTextChatMessage);
        imageViewLogout = rootView.findViewById(R.id.imageViewLogout);
        imageViewAddImage = rootView.findViewById(R.id.imageViewAddImage);
        imageViewSendMessage = rootView.findViewById(R.id.imageViewSendMessage);

        message = new Message();
        currentUser = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                    String val = expenseSnap.getValue(String.class);
                    currentUser.add(val);
                }

                textViewChatName.setText(currentUser.get(1)+" "+currentUser.get(2));
                //Toast.makeText(getActivity(), "Welcome "+currentUser.get(1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase2 = FirebaseDatabase.getInstance().getReference("messages");

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MainActivity.messages.clear();
                for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                    Message message = expenseSnap.getValue(Message.class);
                    MainActivity.messages.add(message);
                }
                mAdapter.notifyDataSetChanged();
//                Fragment frg = getFragmentManager().findFragmentByTag("tag_chat");
//                final FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(frg);
//                ft.attach(frg);
//                ft.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//        mDatabase = FirebaseDatabase.getInstance().getReference("messages");
//        mDatabase.removeEventListener(listener);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Chat Room");

        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                mListener.goToLoginFromChat();
            }
        });

        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewAddImage.setEnabled(false);
                imageViewLogout.setEnabled(false);
                imageViewSendMessage.setEnabled(false);
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED)
                {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST );
                    imageViewAddImage.setEnabled(true);
                    imageViewLogout.setEnabled(true);
                    imageViewSendMessage.setEnabled(true);
                }
                else {
                    Intent intent = new Intent();
                    // Show only images, no videos or anything else
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select a Picture"), PICK_IMAGE_REQUEST);
                }
            }
        });

        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnected()){
                    if(editTextChatMessage.getText().toString().isEmpty()&&FLAG==0){
                        Toast.makeText(getActivity(), "Nothing to send", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        imageViewAddImage.setEnabled(false);
                        imageViewLogout.setEnabled(false);
                        imageViewSendMessage.setEnabled(false);

                        final String messageInput = editTextChatMessage.getText().toString();

                        if(FLAG==1){
                            imageViewAddImage.setDrawingCacheEnabled(true);
                            imageViewAddImage.buildDrawingCache();
                            Bitmap bitmap = imageViewAddImage.getDrawingCache();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                            imageViewAddImage.setDrawingCacheEnabled(false);
                            byte[] data = baos.toByteArray();

                            String path ="messages/" + UUID.randomUUID() + ".png";
                            final StorageReference messageRef = storage.getReference(path);

                            UploadTask uploadTask = messageRef.putBytes(data);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageViewAddImage.setEnabled(true);
                                    imageViewLogout.setEnabled(true);
                                    imageViewSendMessage.setEnabled(true);

                                    messageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            message.message = messageInput;
                                            message.imageURL = ""+uri;
                                            message.firstName = currentUser.get(1);
                                            message.prettyTime = new Date();

                                            MainActivity.messages.add(message);
                                            mAdapter.notifyDataSetChanged();

                                            mDatabase = FirebaseDatabase.getInstance().getReference("messages");
                                            mDatabase.setValue(MainActivity.messages);

                                            editTextChatMessage.setText("");
                                            imageViewAddImage.setImageResource(R.drawable.addimage);
                                            FLAG=0;

                                            // mListener.goToMainAndComeBackToChat(message);
                                        }
                                    });

                                }
                            });
                        }
                        else {          //When there is no image but just text
                            message.message = messageInput;
                            message.imageURL = "NO_IMAGE";
                            message.firstName = currentUser.get(1);
                            message.prettyTime = new Date();

                            MainActivity.messages.add(message);
                            mAdapter.notifyDataSetChanged();

                            mDatabase = FirebaseDatabase.getInstance().getReference("messages");
                            mDatabase.setValue(MainActivity.messages);

                            editTextChatMessage.setText("");
                            imageViewAddImage.setImageResource(R.drawable.addimage);
                            FLAG=0;

                            imageViewAddImage.setEnabled(true);
                            imageViewLogout.setEnabled(true);
                            imageViewSendMessage.setEnabled(true);

                            //mListener.goToMainAndComeBackToChat(message);

                        }


                    }
                }
                else{
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageViewAddImage.setImageBitmap(bitmap);
                imageViewAddImage.setEnabled(true);
                imageViewLogout.setEnabled(true);
                imageViewSendMessage.setEnabled(true);
                FLAG = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imageViewAddImage.setEnabled(true);
                    imageViewLogout.setEnabled(true);
                    imageViewSendMessage.setEnabled(true);

                    Intent intent = new Intent();
                    // Show only images, no videos or anything else
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select a Picture"), PICK_IMAGE_REQUEST);


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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToLoginFromChat();
        void goToMainAndComeBackToChat(Message message);
    }

    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void deletePost(Message message) {
//            mDatabase = FirebaseDatabase.getInstance().getReference("messages");
//            MainActivity.messages.clear();
//            final Message newMessage = message;
//
//            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
//                        Message message = expenseSnap.getValue(Message.class);
//                        MainActivity.messages.add(message);
//                    }
//                    MainActivity.messages.remove(newMessage);
//                    mDatabase.setValue(MainActivity.messages);
//
//                    mAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(getActivity(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
//                }
//            });

            MainActivity.messages.remove(message);
            mAdapter.notifyDataSetChanged();
            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference("messages");
            mDatabase.setValue(MainActivity.messages);
        }
    };


    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            MainActivity.messages.clear();
            for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                Message message = expenseSnap.getValue(Message.class);
                MainActivity.messages.add(message);
            }

            Fragment frg = getFragmentManager().findFragmentByTag("tag_chat");
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(frg);
//            ft.attach(frg);
//            ft.commit();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getActivity(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType()!=ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType()!=ConnectivityManager.TYPE_MOBILE)){
            return false;
        }
        return true;

    }

}
