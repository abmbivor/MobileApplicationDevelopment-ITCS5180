package net.ddns.bivor.inclass08;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class EditExpenseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<String> category;

    Expense expense;
    EditText editTextEditExpenseName, editTextEditExpenseAmount;
    TextView textViewEditExpenseSelectCategory;
    Button buttonEditSave, buttonEditCancel;
    private EditExpenseListener mListener;
    int index;


    public EditExpenseFragment() {
        // Required empty public constructor
    }

    public EditExpenseFragment(Expense expense, int index) {
        this.expense = expense;
        this.index = index;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        category = new ArrayList<>(Arrays.asList("Groceries","Invoice","Transportation","Shopping", "Rent", "Trips",
                "Utilities","Other"));

        View rootView = inflater.inflate(R.layout.fragment_edit_expense, container, false);
        // Inflate the layout for this fragment





        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditExpenseFragment.EditExpenseListener) {
            mListener = (EditExpenseFragment.EditExpenseListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EditExpenseListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Edit Expense");

        editTextEditExpenseName = getActivity().findViewById(R.id.editTextEditExpenseName);
        editTextEditExpenseAmount = getActivity().findViewById(R.id.editTextEditExpenseAmount);
        textViewEditExpenseSelectCategory = getActivity().findViewById(R.id.textViewEditExpenseSelectCategory);

        editTextEditExpenseName.setText(expense.name);
        editTextEditExpenseAmount.setText(expense.amount);
        textViewEditExpenseSelectCategory.setText(expense.category);

        getActivity().findViewById(R.id.textViewEditExpenseSelectCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Choose a Keyword")
                        .setItems(category.toArray(new CharSequence[category.size()]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textViewEditExpenseSelectCategory.setText(category.get(which));
                            }
                        });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        getActivity().findViewById(R.id.buttonEditCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToShowFromEditOnCancel();
            }
        });

        getActivity().findViewById(R.id.buttonEditSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextEditExpenseName.getText().toString().isEmpty()){
                    editTextEditExpenseName.setError("Enter Expense Name");
                    Toast.makeText(getActivity(), "Enter Expense Name", Toast.LENGTH_SHORT).show();
                }
                else if(editTextEditExpenseAmount.getText().toString().isEmpty()){
                    editTextEditExpenseAmount.setError("Enter Expense Name");
                    Toast.makeText(getActivity(), "Enter Expense Amount", Toast.LENGTH_SHORT).show();
                }
                else if (textViewEditExpenseSelectCategory.getText().toString().equals("Select Category")){
                    Toast.makeText(getActivity(), "Select a Category", Toast.LENGTH_SHORT).show();
                }
                else {
                    expense.name = editTextEditExpenseName.getText().toString();
                    expense.amount = editTextEditExpenseAmount.getText().toString();
                    expense.category = textViewEditExpenseSelectCategory.getText().toString();
                    expense.date = new Date();

                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("expenses").child(""+index).setValue(expense);

                    mListener.goToExpenseFromEditOnSave();

                }
            }
        });

    }


    public interface EditExpenseListener{
        void goToShowFromEditOnCancel();
        void goToExpenseFromEditOnSave();
    }
}
