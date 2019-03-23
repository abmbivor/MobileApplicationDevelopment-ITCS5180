package net.ddns.bivor.hw04_group14;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerViewIngredients;
    RecyclerView.Adapter mAdapter;
    ArrayList<String> ingredientList;
    ArrayList<Recipe> recipes;
    String URL;
    ProgressBar pb;
    TextView textViewLoading, textView, textView2;
    Button buttonSearch;
    EditText editTextDishTitle;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        // 1. get a reference to recyclerView
        recyclerViewIngredients = (RecyclerView) rootView.findViewById(R.id.recyclerViewIngredients);

        // 2. set layoutManger
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));

        ingredientList = new ArrayList<>();
        //ingredientList.add("");

        mAdapter = new IngredientAdapter(ingredientList, communication);
        recyclerViewIngredients.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        pb = rootView.findViewById(R.id.progressBar);
        textViewLoading = rootView.findViewById(R.id.textViewLoading);
        pb.setVisibility(View.INVISIBLE);
        textViewLoading.setVisibility(View.INVISIBLE);

        textView = rootView.findViewById(R.id.textView);
        textView2 = rootView.findViewById(R.id.textView2);
        buttonSearch = rootView.findViewById(R.id.buttonSearch);

        editTextDishTitle = rootView.findViewById(R.id.editTextDish);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Recipe Puppy");

        getActivity().findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextDishTitle.getText().toString().isEmpty())editTextDishTitle.setError("Enter Dish Name");
                else {
                    disableAll();
                    getActivity().setTitle("Recipes");
                    URL = "http://www.recipepuppy.com/api/?i=";
                    if(ingredientList.size()>0){
                        for(int i=0;i<ingredientList.size();i++){
                            URL+=ingredientList.get(i).trim();
                            if(i<ingredientList.size()- 1)URL+=",";
                            else URL+="&q="+editTextDishTitle.getText().toString().trim();
                        }
                    }
                    else {
                        URL = "http://www.recipepuppy.com/api/?q="+editTextDishTitle.getText().toString().trim();
                    }
                    new getRecipeAsync().execute(URL);
                }
                }
        });
    }



    private class getRecipeAsync extends AsyncTask<String,Integer, ArrayList<Recipe>>{
        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            if(recipes.size()>0){
                enableAll();
                communication.goToRecipe(recipes);
            }
            else {
                enableAll();
                getActivity().setTitle("Recipe Puppy");
                Toast.makeText(getContext(), "No Results Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(values[0]>0 && values[1] == 0){
                pb.setMax(values[0]);
            }
            else if (values[0]>0 && values[1] != 0){
                pb.setProgress(values[1]);
            }
        }

        @Override
        protected ArrayList<Recipe> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            ArrayList<Recipe> result = new ArrayList<>();

            try {
                java.net.URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray recipes = root.getJSONArray("results");

                    publishProgress(recipes.length(),0);

                    for (int i=0;i<recipes.length();i++) {
                        JSONObject recipeJson = recipes.getJSONObject(i);
                        Recipe recipe = new Recipe();
                        recipe.title = recipeJson.getString("title");
                        recipe.thumbnail = recipeJson.getString("thumbnail");
                        recipe.ingredients = recipeJson.getString("ingredients");
                        recipe.href = recipeJson.getString("href");

                        result.add(recipe);
                        publishProgress(recipes.length(),1);
                    }
                }
            } catch (IOException  e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                //Close the connections
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }

    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void goToRecipe(ArrayList<Recipe> recipes) {
            RecipesFragment recipesFragment=new RecipesFragment();
            Bundle bundle=new Bundle();
            bundle.putSerializable("RECIPES_KEY",recipes);
            recipesFragment.setArguments(bundle);
            FragmentManager manager=getFragmentManager();
            manager.popBackStack();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.container,recipesFragment)
                    .addToBackStack(null).commit();
        }

        @Override
        public void goToSearch() {

        }

        @Override
        public void addIngredient(ArrayList<String> ListOfIngredient) {
                ingredientList = ListOfIngredient;
                mAdapter.notifyDataSetChanged();
        }

        @Override
        public void removeIngredient(ArrayList<String> ListOfIngredient) {
            ingredientList = ListOfIngredient;
            mAdapter.notifyDataSetChanged();
        }
    };

    void disableAll(){
        textView.setVisibility(View.INVISIBLE);
        textView2.setVisibility(View.INVISIBLE);
        buttonSearch.setVisibility(View.INVISIBLE);
        editTextDishTitle.setVisibility(View.INVISIBLE);
        recyclerViewIngredients.setVisibility(View.INVISIBLE);
        textViewLoading.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
    }
    void enableAll(){
        textView.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        buttonSearch.setVisibility(View.VISIBLE);
        editTextDishTitle.setVisibility(View.VISIBLE);
        recyclerViewIngredients.setVisibility(View.VISIBLE);
        textViewLoading.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.INVISIBLE);
    }

}
