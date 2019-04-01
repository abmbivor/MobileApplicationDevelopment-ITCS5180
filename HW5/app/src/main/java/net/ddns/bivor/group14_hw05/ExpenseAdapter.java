package net.ddns.bivor.group14_hw05;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder>{
    ArrayList<Expense> mData;
    private FragmentCommunication mCommunicator;

    public ExpenseAdapter(ArrayList<Expense> mData, FragmentCommunication mCommunicator) {
        this.mData = mData;
        this.mCommunicator = mCommunicator;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mCommunicator);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Expense expense = mData.get(i);
        viewHolder.textViewExpenseItem.setText(expense.name);
        viewHolder.textViewItemCost.setText("Cost: $"+ expense.cost);
        viewHolder.textViewItemDate.setText("Date: "+expense.datePicked);
        viewHolder.expense = expense;
        viewHolder.expenses = mData;
        viewHolder.selectedIndex = i;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewExpenseItem, textViewItemCost, textViewItemDate;
        Expense expense;
        int selectedIndex;
        ArrayList<Expense> expenses;
        FragmentCommunication mCommunication;
        ImageView imageViewEdit;

        public ViewHolder(@NonNull final View itemView, FragmentCommunication Communicator) {
            super(itemView);

            textViewExpenseItem = itemView.findViewById(R.id.textViewExpenseName);
            textViewItemCost = itemView.findViewById(R.id.textViewExpenseCost);
            textViewItemDate = itemView.findViewById(R.id.textViewExpenseDate);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);

            mCommunication = Communicator;

            itemView.setOnClickListener(this);
            imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommunication.respondEdit(expense,selectedIndex);
                }
            });
        }

        @Override
        public void onClick(View v) {
            mCommunication.respond(expense, selectedIndex);
        }

    }
}
