package net.ddns.bivor.group14_hw05;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ExpenseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;

    TextView textViewTotalExpenseValue;

    public ExpenseFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_expense, container, false);
        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ExpenseAdapter(MainActivity.expenses, communication);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        textViewTotalExpenseValue = rootView.findViewById(R.id.textViewTotalExpenseValue);

        double total = 0.0;
        for(Expense expense: MainActivity.expenses){
            total+= Double.parseDouble(expense.cost);
        }
        textViewTotalExpenseValue.setText("$"+total);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Expense Manager");

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToAddExpense();
    }

    FragmentCommunication communication=new FragmentCommunication() {
        @Override
        public void respond(Expense expense, int index) {
            DisplayExpenseFragment displayExpense=new DisplayExpenseFragment();
            Bundle bundle=new Bundle();
            bundle.putSerializable("EXPENSE_KEY",expense);
            bundle.putInt("INDEX_KEY", index);
            displayExpense.setArguments(bundle);
            FragmentManager manager=getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.container,displayExpense)
                    .addToBackStack(null).commit();
        }

        public void delete(Expense expense){
            MainActivity.expenses.remove(expense);
            mAdapter.notifyDataSetChanged();
            if(MainActivity.expenses.size()<1)getActivity().findViewById(R.id.textViewExpenseMessage).setVisibility(View.VISIBLE);
        }

        @Override
        public void respondEdit(Expense expense, int index) {
            EditExpenseFragment editExpenseFragment = new EditExpenseFragment(expense, index);
            FragmentManager manager=getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.container,editExpenseFragment)
                    .addToBackStack(null).commit();
        }

    };
}
