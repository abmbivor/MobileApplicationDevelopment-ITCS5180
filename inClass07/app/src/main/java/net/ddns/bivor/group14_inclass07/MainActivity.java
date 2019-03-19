package net.ddns.bivor.group14_inclass07;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ExpenseAppFragment.OnFragmentInteractionListener, AddExpenseFragment.OnFragmentInteractionListener, ShowExpense.OnFragmentInteractionListener{


    public static final ArrayList<Expense> expenses = new ArrayList<>();
    public static final ArrayList<Expense> selectedExpenses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Expense App");

        //expenses = new ArrayList<>();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new ExpenseAppFragment(), "tag_expenseApp").commit();

    }

    @Override
    public void goToAddExpense() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddExpenseFragment(), "tag_addExpense")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToShowExpense(Expense expense) {
        selectedExpenses.add(expense);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ShowExpense(), "tag_showExpense").commit();
    }

    @Override
    public void goToExpenseOnCancel() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ExpenseAppFragment(), "tag_expenseApp").commit();
    }

    @Override
    public void goToExpenseOnAdd(Expense expense) {
        expenses.add(expense);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ExpenseAppFragment(), "tag_expenseApp").commit();
    }

    @Override
    public void goToExpenseFromShow() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ExpenseAppFragment(), "tag_expenseApp").commit();
    }
}
