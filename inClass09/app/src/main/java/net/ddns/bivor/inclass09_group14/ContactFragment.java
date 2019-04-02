package net.ddns.bivor.inclass09_group14;

import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ContactFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;

    Button buttonCreateContact;
    ImageView imageViewLogOut;


    public ContactFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ContactAdapter(MainActivity.contacts, communication);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        buttonCreateContact = rootView.findViewById(R.id.buttonCreateContact);
        imageViewLogOut = rootView.findViewById(R.id.imageViewLogOut);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Contacts");

        buttonCreateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToCreateNewContact();
            }
        });

        imageViewLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                mListener.goToLogInFromContact();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToCreateNewContact();
        void goToLogInFromContact();
    }

    OnFragmentListener communication=new OnFragmentListener() {
        @Override
        public void delete(Contact contact){
            MainActivity.contacts.remove(contact);
            mAdapter.notifyDataSetChanged();
            FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();
            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
            mDatabase.child("contacts").setValue(MainActivity.contacts);
        }

    };

}
