package net.ddns.bivor.group14_hw06;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SignUpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;

    EditText editTextSignUpFirstName, editTextSignUpLastName, editTextSignUpEmail, editTextSignUpPassword,editTextSignUpRepeatPassword;
    Button buttonSignUpSignUp, buttonSignUpLogin;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        editTextSignUpFirstName = rootView.findViewById(R.id.editTextSignUpFirstName);
        editTextSignUpLastName = rootView.findViewById(R.id.editTextSignUpLastName);
        editTextSignUpEmail = rootView.findViewById(R.id.editTextSignUpEmail);
        editTextSignUpPassword = rootView.findViewById(R.id.editTextSignUpPassword);
        editTextSignUpRepeatPassword = rootView.findViewById(R.id.editTextSignUpRepeatPassword);
        buttonSignUpSignUp = rootView.findViewById(R.id.buttonSignUpSignUp);
        buttonSignUpLogin = rootView.findViewById(R.id.buttonSignUpLogin);

        mAuth = FirebaseAuth.getInstance();

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Sign Up");

        buttonSignUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToLoginFromSignup();
            }
        });

        buttonSignUpSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignUpSignUp.setEnabled(false);
                buttonSignUpLogin.setEnabled(false);

                final String firstName = editTextSignUpFirstName.getText().toString().trim();
                final String lastName = editTextSignUpLastName.getText().toString().trim();
                final String email = editTextSignUpEmail.getText().toString().trim();
                final String password = editTextSignUpPassword.getText().toString();
                final String confirmPassword = editTextSignUpRepeatPassword.getText().toString();

                if(firstName.isEmpty()){
                    editTextSignUpFirstName.setError("Enter First Name");
                }
                else if(lastName.isEmpty()){
                    editTextSignUpLastName.setError("Enter Last Name");
                }
                else if(email.isEmpty()){
                    editTextSignUpEmail.setError("Enter Email");
                }
                else if(password.isEmpty()){
                    editTextSignUpPassword.setError("Enter Password");
                }
                else if(confirmPassword.isEmpty()){
                    editTextSignUpRepeatPassword.setError("Confirm Password");
                }
                else if(!password.equals(confirmPassword)){
                    editTextSignUpRepeatPassword.setError("Passwords don't match");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    editTextSignUpEmail.setError("Enter a valid email address");
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
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid());
                                        mDatabase.setValue(new User(firstName,lastName,email,password));

                                        buttonSignUpSignUp.setEnabled(true);
                                        buttonSignUpLogin.setEnabled(true);

                                        Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();

                                        mListener.goToChatFromSignup();

                                    }
                                    else {
                                        buttonSignUpSignUp.setEnabled(true);
                                        buttonSignUpLogin.setEnabled(true);

                                        Toast.makeText(getActivity(), "SignUp Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                buttonSignUpSignUp.setEnabled(true);
                buttonSignUpLogin.setEnabled(true);
            }
        });

        editTextSignUpPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_SPACE){
                    editTextSignUpPassword.setError("Can not have space!");
                }

                return false;
            }
        });
        editTextSignUpRepeatPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_SPACE){
                    editTextSignUpRepeatPassword.setError("Can not have space!");
                }

                return false;
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
        void goToLoginFromSignup();
        void goToChatFromSignup();
    }
}
