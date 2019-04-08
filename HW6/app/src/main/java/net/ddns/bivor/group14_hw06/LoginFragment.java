package net.ddns.bivor.group14_hw06;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;

    Button buttonLogInLogIn, buttonLogInSignUp;
    EditText editTextLogInEmail, editTextLogInPassword;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        editTextLogInEmail = rootView.findViewById(R.id.editTextLoginEmail);
        editTextLogInPassword = rootView.findViewById(R.id.editTextLoginPassword);
        buttonLogInLogIn = rootView.findViewById(R.id.buttonLoginLogin);
        buttonLogInSignUp = rootView.findViewById(R.id.buttonLoginSignUp);

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
                mListener.goToSignupFromLogin();
            }
        });

        buttonLogInLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLogInLogIn.setEnabled(false);
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
                                buttonLogInLogIn.setEnabled(true);
                                buttonLogInSignUp.setEnabled(true);

                                mListener.goToChatFromLogin();
                            }
                            else{
                                buttonLogInLogIn.setEnabled(true);
                                buttonLogInSignUp.setEnabled(true);
                                Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                buttonLogInLogIn.setEnabled(true);
                buttonLogInSignUp.setEnabled(true);

            }
        });

        editTextLogInPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_SPACE){
                    editTextLogInPassword.setError("Can not have space!");
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
        void goToSignupFromLogin();
        void goToChatFromLogin();
    }
}
