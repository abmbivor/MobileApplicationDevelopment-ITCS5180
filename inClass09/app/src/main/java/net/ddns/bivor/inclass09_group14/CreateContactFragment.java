package net.ddns.bivor.inclass09_group14;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class CreateContactFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    Contact contact;

    ImageView imageView;
    EditText editTextCreateContactName, editTextCreateContactEmail, editTextCreateContactPhone;
    Button buttonSubmit;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private int CAPTURE_IMAGE_REQUEST = 2;
    public  static final int MY_PERMISSIONS_REQUEST = 11;

    public CreateContactFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_create_contact, container, false);

        imageView = rootView.findViewById(R.id.imageView);
        editTextCreateContactName = rootView.findViewById(R.id.editTextCreateContactName);
        editTextCreateContactEmail = rootView.findViewById(R.id.editTextCreateContactEmail);
        editTextCreateContactPhone = rootView.findViewById(R.id.editTextCreateContactPhone);
        buttonSubmit = rootView.findViewById(R.id.buttonSubmit);

        contact = new Contact();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Create New Contact");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST );
                }
                else {
                    Intent intent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent2, CAPTURE_IMAGE_REQUEST);
                }
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSubmit.setEnabled(false);

                final String name = editTextCreateContactName.getText().toString().trim();
                final String email = editTextCreateContactEmail.getText().toString().trim();
                final String phone = editTextCreateContactPhone.getText().toString().trim();

                if(name.isEmpty()){
                    editTextCreateContactName.setError("Enter Name");
                }
                else if(email.isEmpty()){
                    editTextCreateContactEmail.setError("Enter Email");
                }
                else if(phone.isEmpty()){
                    editTextCreateContactPhone.setError("Enter Phone");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    editTextCreateContactEmail.setError("Enter a valid email address");
                }
                else {
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    imageView.setDrawingCacheEnabled(false);
                    byte[] data = baos.toByteArray();

                    String path ="contacts/" + UUID.randomUUID() + ".png";
                    final StorageReference receiptRef = storage.getReference(path);

                    UploadTask uploadTask = receiptRef.putBytes(data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            buttonSubmit.setEnabled(true);

                            receiptRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    contact.imageURL = ""+uri;
                                    contact.name = name;
                                    contact.email = email;
                                    contact.phone = phone;


                                    mListener.goToContactFromCreateNewContact(contact);
                                }
                            });
                        }
                    });
                }
                buttonSubmit.setEnabled(true);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent2, CAPTURE_IMAGE_REQUEST);
                } else {
                    Toast.makeText(getActivity(), "Camera Permission Not Given", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(thumbnail);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToContactFromCreateNewContact(Contact contact);
    }

}
