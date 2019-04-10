package net.ddns.bivor.group14_ic10;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    Button buttonLogInLogIn, buttonLogInSignUp;
    EditText editTextLogInEmail, editTextLogInPassword;

    private OnFragmentInteractionListener mListener;

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
                    OkHttpClient client = new OkHttpClient();

                    RequestBody formBody = new FormBody.Builder()
                            .add("email", email)
                            .add("password", password)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/login")
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
                                    Toast.makeText(getActivity(), "Failed to Authorize", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String jsonData = responseBody.string();
                                    JSONObject jsonObject = new JSONObject(jsonData);

                                    if(jsonObject.getString("token").equals("null")){
                                        if(getActivity()!=null){
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), "Failed to Authorize", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            });
                                        }

                                    }
                                    else {
                                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("TOKEN", jsonObject.getString("token"));
                                        editor.apply();
                                        mListener.goToNotesFromLogin();
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                buttonLogInLogIn.setEnabled(true);
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
        void goToNotesFromLogin();
    }
}
