package net.ddns.bivor.inclass08;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExpenseAppFragment.OnFragmentInteractionListener, AddExpenseFragment.OnFragmentInteractionListener, ShowExpense.OnFragmentInteractionListener, EditExpenseFragment.EditExpenseListener{


    private DatabaseReference mDatabase;
    public static final ArrayList<Expense> expenses = new ArrayList<>();
    public static final ArrayList<Expense> selectedExpenses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Expense App");

        //expenses = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("expenses");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                    Expense expense = expenseSnap.getValue(Expense.class);
                    expenses.add(expense);
                }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new ExpenseAppFragment(), "tag_expenseApp").commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

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
        mDatabase.setValue(expenses);

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

    @Override
    public void goToEditExpenseFromShow(Expense expense, int index) {
        //getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new EditExpenseFragment(expense, index), "tag_editExpense").commit();
    }

    @Override
    public void goToShowFromEditOnCancel() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ExpenseAppFragment(), "tag_expenseApp").commit();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, new ShowExpense(), "tag_showExpense").commit();
    }

    @Override
    public void goToExpenseFromEditOnSave() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ExpenseAppFragment(), "tag_expenseApp").commit();
    }
}
