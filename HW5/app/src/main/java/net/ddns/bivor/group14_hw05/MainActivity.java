package net.ddns.bivor.group14_hw05;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements ExpenseFragment.OnFragmentInteractionListener,
        AddExpenseFragment.OnFragmentInteractionListener, DisplayExpenseFragment.OnFragmentInteractionListener,
        EditExpenseFragment.OnFragmentInteractionListener{

    public static final ArrayList<Expense> expenses = new ArrayList<>();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("Expense Manager");

        toolbar.setLogo(R.drawable.app);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.menu));

        mDatabase = FirebaseDatabase.getInstance().getReference("expenses");

        expenses.clear();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot expenseSnap: dataSnapshot.getChildren()){
                    Expense expense = expenseSnap.getValue(Expense.class);
                    expenses.add(expense);
                }
                Collections.sort(expenses,Expense.COMPARE_BY_DATE);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new ExpenseFragment(), "tag_expenseApp").commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sortByCost:
                Collections.sort(expenses,Expense.COMPARE_BY_COST);
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ExpenseFragment(), "tag_expenseApp").commit();
                return true;
            case R.id.sortByDate:
                Collections.sort(expenses,Expense.COMPARE_BY_DATE);
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ExpenseFragment(), "tag_expenseApp").commit();
                return true;
            case R.id.resetAll:
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("RESET Option")
                        .setMessage(R.string.alertDialogueMessage)
                        .setPositiveButton(R.string.alertDialoguePositive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                expenses.clear();
                                mDatabase.setValue(expenses);
//                                FirebaseStorage storage = FirebaseStorage.getInstance();
//                                StorageReference mRef = storage.getReference("receipts/");
//                                mRef.delete();
                                dialog.dismiss();
                                getSupportFragmentManager().popBackStack();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, new ExpenseFragment(), "tag_expenseApp").commit();

                            }
                        })
                        .setNegativeButton(R.string.alertDialogueNegative, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void goToAddExpense() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddExpenseFragment(), "tag_addExpense")
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void goToExpenseOnAdd(Expense expense) {
        expenses.add(expense);
        Collections.sort(expenses,Expense.COMPARE_BY_DATE);
        mDatabase.setValue(expenses);

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ExpenseFragment(), "tag_expenseApp").commit();
    }

    @Override
    public void goToExpenseFromShow() {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ExpenseFragment(), "tag_expenseApp").commit();
    }

    @Override
    public void goToExpenseFromEditOnSave() {
        Collections.sort(expenses,Expense.COMPARE_BY_DATE);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ExpenseFragment(), "tag_expenseApp").commit();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
//        if(getFragmentManager().getBackStackEntryCount()>0){
//            getFragmentManager().popBackStack();
//        }
//        else {
//            finish();
//            super.onBackPressed();
//        }

    }
}
