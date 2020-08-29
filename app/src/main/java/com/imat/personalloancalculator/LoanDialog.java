package com.imat.personalloancalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.NumberFormat;

public class LoanDialog extends DialogFragment {

    TextView valueMonthlyRepayment;
    TextView valueTotalAmountPayable;
    TextView valueFullAmount;
    Button okBtn;

    double loanAmount;
    double interestRate;
    double monthlyRepayment;
    double totalPayment;


    public LoanDialog(double loanAmount, double interestRate, double monthlyRepayment, double totalPayment) {
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.monthlyRepayment = monthlyRepayment;
        this.totalPayment = totalPayment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.loan_dialog, null);

        valueMonthlyRepayment = view.findViewById(R.id.valueMonthlyRepayment);
        valueTotalAmountPayable = view.findViewById(R.id.valueTotalAmountPayable);
        valueFullAmount = view.findViewById(R.id.valueFullAmount);
        okBtn = view.findViewById(R.id.okBtn);

        String currencyString = "LKR ";

        valueMonthlyRepayment.setText(String.format("%s%s", currencyString, NumberFormat.getCurrencyInstance().format(monthlyRepayment).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), "")));
        valueTotalAmountPayable.setText(String.format("%s%s", currencyString, NumberFormat.getCurrencyInstance().format(totalPayment).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), "")));
        valueFullAmount.setText(String.format("%s%s", currencyString, NumberFormat.getCurrencyInstance().format(loanAmount).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), "")));

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOK();
            }
        });

        builder.setView(view);
        builder.setBackground(new ColorDrawable(Color.TRANSPARENT))
                .setBackgroundInsetStart(10)
                .setBackgroundInsetEnd(10);

        return builder.create();
    }

    private void onOK() {
        this.dismiss();
    }
}
