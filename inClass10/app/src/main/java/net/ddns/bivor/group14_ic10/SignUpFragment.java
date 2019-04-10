package net.ddns.bivor.group14_ic10;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SignUpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

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


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Sign Up");

        buttonSignUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToLoginFromSignUp();
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
                    OkHttpClient client = new OkHttpClient();

                    RequestBody formBody = new FormBody.Builder()
                            .add("name", firstName+" "+lastName)
                            .add("email", email)
                            .add("password", password)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/register")
                            .header("Content-Type","application/x-www-form-urlencoded")
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(getActivity(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful()) {
                                    if(getActivity()!=null){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), "Failed to Sign Up", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }
                                else {
                                    String jsonData = responseBody.string();
                                    JSONObject jsonObject = new JSONObject(jsonData);

                                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("TOKEN", jsonObject.getString("token"));
                                    editor.apply();
                                    MainActivity.notes.clear();
                                    mListener.goToNotesFromSignUp();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                buttonSignUpSignUp.setEnabled(true);
                buttonSignUpLogin.setEnabled(true);
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
        void goToLoginFromSignUp();
        void goToNotesFromSignUp();
    }
}
