package net.ddns.bivor.group14_hw05;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class DisplayExpenseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Expense expense;
    int index;

    TextView textViewDisplayName, textViewDisplayCost, textViewDisplayDate;
    ImageView imageViewDisplayReceipt;
    Button buttonClose;
    ProgressBar pb;

    private OnFragmentInteractionListener mListener;

    public DisplayExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            expense = (Expense) getArguments().getSerializable("EXPENSE_KEY");
            index =  getArguments().getInt("INDEX_KEY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_display_expense, container, false);

        textViewDisplayName = rootView.findViewById(R.id.textViewDisplayName);
        textViewDisplayCost = rootView.findViewById(R.id.textViewDisplayCost);
        textViewDisplayDate = rootView.findViewById(R.id.textViewDisplayDate);
        imageViewDisplayReceipt = rootView.findViewById(R.id.imageViewDisplayReceipt);
        buttonClose = rootView.findViewById(R.id.buttonClose);
        buttonClose.setEnabled(false);
        pb = rootView.findViewById(R.id.progressBarDisplay);
        pb.setVisibility(View.INVISIBLE);

        textViewDisplayName.setText(expense.name);
        textViewDisplayCost.setText("$ "+ expense.cost);
        textViewDisplayDate.setText(expense.datePicked);

        pb.setVisibility(View.VISIBLE);
        Picasso.get().load(expense.imageURL).into(imageViewDisplayReceipt, new Callback() {
            @Override
            public void onSuccess() {
                pb.setVisibility(View.INVISIBLE);
                buttonClose.setEnabled(true);
            }

            @Override
            public void onError(Exception e) {
                pb.setVisibility(View.INVISIBLE);
                buttonClose.setEnabled(true);
                Toast.makeText(getActivity(), "No Image Found", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToExpenseFromShow();
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
        void goToExpenseFromShow();
    }

}
