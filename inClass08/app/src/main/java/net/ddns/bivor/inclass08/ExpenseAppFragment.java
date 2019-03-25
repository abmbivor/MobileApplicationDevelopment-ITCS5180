package net.ddns.bivor.inclass08;

import android.content.Context;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExpenseAppFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ExpenseAppFragment extends Fragment {

    public OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    //RecyclerView.LayoutManager layoutManager;

    public ExpenseAppFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense_app, container, false);
        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ExpenseAdapter(MainActivity.expenses, communication);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Expense App");

        if(MainActivity.expenses.size()>0)getActivity().findViewById(R.id.textViewExpenseMessage).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToAddExpense();
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
        void goToAddExpense();
        public void goToShowExpense(Expense expense);
    }
    FragmentCommunication communication=new FragmentCommunication() {
        @Override
        public void respond(Expense expense) {
            ShowExpense showExpense=new ShowExpense();
            Bundle bundle=new Bundle();
            bundle.putSerializable("EXPENSE_KEY",expense);
            showExpense.setArguments(bundle);
            FragmentManager manager=getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.container,showExpense)
                    .addToBackStack(null).commit();
        }

        public void delete(Expense expense){
            MainActivity.expenses.remove(expense);
            mAdapter.notifyDataSetChanged();
            if(MainActivity.expenses.size()<1)getActivity().findViewById(R.id.textViewExpenseMessage).setVisibility(View.VISIBLE);
        }

    };

    
}
