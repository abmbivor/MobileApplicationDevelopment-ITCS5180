package net.ddns.bivor.hw04_group14;

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
import android.widget.LinearLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RecipesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerViewRecipe;
    RecyclerView.Adapter mAdapter;

    ArrayList<Recipe> recipes;

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipes = new ArrayList<>();
        if (getArguments() != null) {
            recipes = (ArrayList<Recipe>) getArguments().getSerializable("RECIPES_KEY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);
        // 1. get a reference to recyclerView
        recyclerViewRecipe = (RecyclerView) rootView.findViewById(R.id.recyclerViewRecipe);

        // 2. set layoutManger
        recyclerViewRecipe.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mAdapter = new RecipeAdapter(recipes, communication);
        recyclerViewRecipe.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Recipes");

        getActivity().findViewById(R.id.buttonFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communication.goToSearch();
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
        void onFragmentInteraction(Uri uri);
    }

    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void goToRecipe(ArrayList<Recipe> recipes) {

        }

        @Override
        public void goToSearch() {
            SearchFragment searchFragment=new SearchFragment();
            FragmentManager manager=getFragmentManager();
            manager.popBackStack();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.container,searchFragment)
                    .addToBackStack(null).commit();
        }

        @Override
        public void addIngredient(ArrayList<String> ListOfIngredient) {

        }

        @Override
        public void removeIngredient(ArrayList<String> ListOfIngredient) {

        }
    };
}
