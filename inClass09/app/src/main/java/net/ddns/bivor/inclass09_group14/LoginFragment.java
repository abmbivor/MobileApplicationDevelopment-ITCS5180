package net.ddns.bivor.inclass09_group14;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;

    Button buttonLogIn, buttonLogInSignUp;
    EditText editTextLogInEmail, editTextLogInPassword;

    public LoginFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        editTextLogInEmail = rootView.findViewById(R.id.editTextLogInEmail);
        editTextLogInPassword = rootView.findViewById(R.id.editTextLogInPassword);
        buttonLogIn = rootView.findViewById(R.id.buttonLogIn);
        buttonLogInSignUp = rootView.findViewById(R.id.buttonLogInSignUp);

        mAuth = FirebaseAuth.getInstance();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Login");

        buttonLogInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSignUp();
            }
        });

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLogIn.setEnabled(false);
                buttonLogInSignUp.setEnabled(false);

                final String email = editTextLogInEmail.getText().toString().trim();
                final String password = editTextLogInPassword.getText().toString();

                if(email.isEmpty()){
                    editTextLogInEmail.setError("Enter Email");
                }
                else if(password.isEmpty()){
                    editTextLogInPassword.setError("Enter Password");
                }
                else {
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                buttonLogIn.setEnabled(true);
                                buttonLogInSignUp.setEnabled(true);

                                mListener.goToContactFromLogIn();
                            }
                            else{
                                buttonLogIn.setEnabled(true);
                                buttonLogInSignUp.setEnabled(true);
                                Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                buttonLogIn.setEnabled(true);
                buttonLogInSignUp.setEnabled(true);

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
        void goToSignUp();
        void goToContactFromLogIn();
    }

}
