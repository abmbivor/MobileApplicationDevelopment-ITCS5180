package net.ddns.bivor.inclass09_group14;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class SignUpFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    EditText editTextFirstName, editTextLastName, editTextSignUpEmail, editTextSignUpPassword,editTextSignUpConfirmPassword;
    Button buttonSignUp, buttonCancel;

    public SignUpFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        editTextFirstName = rootView.findViewById(R.id.editTextFirstName);
        editTextLastName = rootView.findViewById(R.id.editTextLastName);
        editTextSignUpEmail = rootView.findViewById(R.id.editTextSignUpEmail);
        editTextSignUpPassword = rootView.findViewById(R.id.editTextSignUpPassword);
        editTextSignUpConfirmPassword = rootView.findViewById(R.id.editTextSignUpConfirmPassword);
        buttonSignUp = rootView.findViewById(R.id.buttonSignUp);
        buttonCancel = rootView.findViewById(R.id.buttonCancel);

        mAuth = FirebaseAuth.getInstance();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Sign Up");


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToLogInFromSignUp();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignUp.setEnabled(false);
                buttonCancel.setEnabled(false);

                final String firstName = editTextFirstName.getText().toString().trim();
                final String lastName = editTextLastName.getText().toString().trim();
                final String email = editTextSignUpEmail.getText().toString().trim();
                final String password = editTextSignUpPassword.getText().toString();
                final String confirmPassword = editTextSignUpConfirmPassword.getText().toString();

                if(firstName.isEmpty()){
                    editTextFirstName.setError("Enter First Name");
                }
                else if(lastName.isEmpty()){
                    editTextLastName.setError("Enter Last Name");
                }
                else if(email.isEmpty()){
                    editTextSignUpEmail.setError("Enter Email");
                }
                else if(password.isEmpty()){
                    editTextSignUpPassword.setError("Enter Password");
                }
                else if(confirmPassword.isEmpty()){
                    editTextSignUpConfirmPassword.setError("Confirm Password");
                }
                else if(!password.equals(confirmPassword)){
                    editTextSignUpConfirmPassword.setError("Passwords don't match");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    editTextFirstName.setError("Enter a valid email address");
                }
                else if(password.length()<6){
                    editTextSignUpPassword.setError("Password length should be atleast 6!");
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        ArrayList<Contact> contacts = new ArrayList<>();
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
                                        mDatabase.setValue(new User(firstName,lastName,email,password,contacts));

                                        buttonSignUp.setEnabled(true);
                                        buttonCancel.setEnabled(true);

                                        mListener.goToContactFromSignUp();

                                    }
                                    else {
                                        buttonSignUp.setEnabled(true);
                                        buttonCancel.setEnabled(true);

                                        Toast.makeText(getActivity(), "SignUp Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

                buttonSignUp.setEnabled(true);
                buttonCancel.setEnabled(true);

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
        void goToLogInFromSignUp();
        void goToContactFromSignUp();
    }

}
