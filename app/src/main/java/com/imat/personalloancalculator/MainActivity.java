package com.imat.personalloancalculator;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    ArrayList<CalculatedLoan> recentlyCalculatedLoans;
    RecentListAdapter recentListAdapter;
    RecyclerView recyclerViewRecentCalcs;

    TextInputLayout textInputAmountLayout;
    TextInputLayout textInputTermLayout;
    TextInputLayout textInputRateLayout;

    CurrencyEditText amountInput;
    TextInputEditText termInput;
    InterestRateEditText interestRateInput;
    Button calcBtn;

    ImageView emptyListIcon;
    TextView emptyListText;

    double loanAmount = 0;
    int loanTerm = 0;
    double annualInterestRate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputAmountLayout = findViewById(R.id.TextInputAmountLayout);
        textInputTermLayout = findViewById(R.id.TextInputTermLayout);
        textInputRateLayout = findViewById(R.id.TextInputRateLayout);

        amountInput = findViewById(R.id.amountInput);
        termInput = findViewById(R.id.termInput);
        interestRateInput = findViewById(R.id.interestRateInput);
        calcBtn = findViewById(R.id.calcBtn);

        recyclerViewRecentCalcs = findViewById(R.id.recyclerViewRecentCalcs);

        emptyListIcon = findViewById(R.id.emptyListIcon);
        emptyListText = findViewById(R.id.emptyListText);

        db = new DatabaseHelper(MainActivity.this);
        recentlyCalculatedLoans = new ArrayList<>();
        loadRecentlyCalculatedLoans();

        recentListAdapter = new RecentListAdapter(MainActivity.this, recentlyCalculatedLoans);
        recyclerViewRecentCalcs.setAdapter(recentListAdapter);
        recyclerViewRecentCalcs.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerViewRecentCalcs.setNestedScrollingEnabled(false);


        //Amount Input Listeners
        amountInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    textInputAmountLayout.setHelperText(null);
                    textInputAmountLayout.setError(validateAmountInput());
                }
            }
        });

        amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputAmountLayout.setError(null);
                textInputAmountLayout.setHelperText(validateAmountInput());
            }
        });

        //Loan Term Input Listeners
        termInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    textInputTermLayout.setHelperText(null);
                    textInputTermLayout.setError(validateTermInput());
                }
            }
        });

        termInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputTermLayout.setError(null);
                textInputTermLayout.setHelperText(validateTermInput());
            }
        });

        //Interest Rate Input Listeners
        interestRateInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    textInputRateLayout.setHelperText(null);
                    textInputRateLayout.setError(validateInterestRateInput());
                }
            }
        });

        interestRateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputRateLayout.setError(null);
                textInputRateLayout.setHelperText(validateInterestRateInput());
            }
        });


        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndOpenDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.clear_recent) {
//            Toast.makeText(this, "Clear", Toast.LENGTH_SHORT).show();
            db.deleteAllData();
            loadRecentlyCalculatedLoans();
            recentListAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    private String validateAmountInput() {
        if (amountInput.getText().toString().isEmpty()) {
            return "Amount cannot be empty!";
        } else if (amountInput.getCleanDoubleValue() < 10000) {
            return "Minimum amount is 10,000.00";
        } else return null;
    }

    private String validateTermInput() {
        if (termInput.getText().toString().isEmpty()) {
            return "Loan term cannot be empty!";
        } else if (Integer.parseInt(termInput.getText().toString()) <= 0) {
            return "Loan term cannot be 0";
        } else return null;
    }

    private String validateInterestRateInput() {
        if (interestRateInput.getText().toString().isEmpty()) {
            return "Interest rate cannot be empty!";
        } else if (interestRateInput.getCleanDoubleValue() <= 0) {
            return "Interest rate cannot be 0.00";
        } else return null;
    }

    private CalculatedLoan calculateLoan(double loanAmount, int loanTerm, double annualInterestRate) {
        //Interest per month
        double i = annualInterestRate / 100 / 12;

        //Number of months
        double n = loanTerm * 12;

        //Monthly Repayment
        double monthlyRepayment = i * loanAmount / (1 - Math.pow(1 + i, -n));

        //Total Payment
        double totalPayment = monthlyRepayment * n;

        return new CalculatedLoan(0, loanAmount, loanTerm, annualInterestRate, monthlyRepayment, totalPayment);
    }

    private void calculateAndOpenDialog() {

        if (validateAmountInput() == null && validateTermInput() == null && validateInterestRateInput() == null) {
            loanAmount = amountInput.getCleanDoubleValue();
            loanTerm = Integer.parseInt(termInput.getText().toString());
            annualInterestRate = interestRateInput.getCleanDoubleValue();

            CalculatedLoan calculatedLoan = calculateLoan(loanAmount, loanTerm, annualInterestRate);

            LoanDialog loanDialog = new LoanDialog(loanAmount, annualInterestRate, calculatedLoan.monthly_payment, calculatedLoan.total_payable_amount);
            loanDialog.show(getSupportFragmentManager(), "loan_dialog");

            db.addCalculation(loanAmount, loanTerm, annualInterestRate, calculatedLoan.monthly_payment, calculatedLoan.total_payable_amount);

            loadRecentlyCalculatedLoans();
            recentListAdapter.notifyDataSetChanged();
        } else {
            textInputAmountLayout.setHelperText(null);
            textInputAmountLayout.setError(validateAmountInput());

            textInputTermLayout.setHelperText(null);
            textInputTermLayout.setError(validateTermInput());

            textInputRateLayout.setHelperText(null);
            textInputRateLayout.setError(validateInterestRateInput());
        }

    }

    void loadRecentlyCalculatedLoans() {
        Cursor cursor = db.readAllData();
        recentlyCalculatedLoans.clear();
        if (cursor.getCount() == 0) {
            emptyListIcon.setVisibility(View.VISIBLE);
            emptyListText.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                double amount = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT));
                int term = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TERM));
                double rate = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_RATE));
                double monthly_payment = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_MONTHLY_REPAYMENT));
                double total_payable_amoun = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL_PAYABLE_AMOUNT));

                CalculatedLoan calculatedLoan = new CalculatedLoan(id, amount, term, rate, monthly_payment, total_payable_amoun);
                recentlyCalculatedLoans.add(calculatedLoan);
            }
            emptyListIcon.setVisibility(View.GONE);
            emptyListText.setVisibility(View.GONE);
        }
    }
}
