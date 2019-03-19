package net.ddns.bivor.group14_inclass07;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowExpense.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowExpense#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowExpense extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Expense expense;

    private OnFragmentInteractionListener mListener;

    public ShowExpense() {
        // Required empty public constructor
    }

    public static ShowExpense newInstance(String param1, String param2) {
        ShowExpense fragment = new ShowExpense();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            expense = (Expense) getArguments().getSerializable("EXPENSE_KEY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_expense, container, false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Show Expense");

        TextView textViewName  = getActivity().findViewById(R.id.textViewDetailNameValue);
        textViewName.setText(expense.name);
        TextView textViewCategory  = getActivity().findViewById(R.id.textViewDetailCategoryValue);
        textViewCategory.setText(expense.category);
        TextView textViewAmount  = getActivity().findViewById(R.id.textViewDetailAmountValue);
        textViewAmount.setText("$ "+ expense.amount);
        TextView textViewDate  = getActivity().findViewById(R.id.textViewDetailDateValue);
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        String d = ft.format(expense.date);
        textViewDate.setText(d.substring(3,5)+"/"+d.substring(0,2)+"/"+d.substring(6,10));

        getActivity().findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToExpenseFromShow();
            }
        });
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
        void goToExpenseFromShow();
    }
}
