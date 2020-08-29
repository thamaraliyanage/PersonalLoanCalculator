package com.imat.personalloancalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class RecentListAdapter extends RecyclerView.Adapter<RecentListAdapter.CalculatedLoanViewHolder> {

    private Context context;
    private ArrayList<CalculatedLoan> calculatedLoans;

    public RecentListAdapter(Context context, ArrayList<CalculatedLoan> calculatedLoans) {
        this.context = context;
        this.calculatedLoans = calculatedLoans;
    }

    @NonNull
    @Override
    public CalculatedLoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recent_list_row,parent,false);
        return  new CalculatedLoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalculatedLoanViewHolder holder, int position) {
        CalculatedLoan calculatedLoan = calculatedLoans.get(position);
        int id = calculatedLoan.id;
        String amount = "LKR "+NumberFormat.getCurrencyInstance().format(calculatedLoan.amount).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), "");
        String term = Integer.toString(calculatedLoan.term);
        String rate = NumberFormat.getCurrencyInstance().format(calculatedLoan.rate).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), "");
        String monthly_payment = "LKR "+NumberFormat.getCurrencyInstance().format(calculatedLoan.monthly_payment).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), "");
        String total_payable_amount = "LKR "+NumberFormat.getCurrencyInstance().format(calculatedLoan.total_payable_amount).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), "");

        String loanSub = "at "+rate+"% for "+term+" Years";

        holder.textViewLoanAmount.setText(amount);
        holder.textViewLoanSub.setText(loanSub);
        holder.textViewMonthlyRepayment.setText(monthly_payment);
        holder.textViewTotalAmountPayable.setText(total_payable_amount);
    }

    @Override
    public int getItemCount() {
        return calculatedLoans.size();
    }

    public class CalculatedLoanViewHolder extends RecyclerView.ViewHolder {

        TextView textViewLoanAmount;
        TextView textViewLoanSub;
        TextView textViewMonthlyRepayment;
        TextView textViewTotalAmountPayable;


        public CalculatedLoanViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLoanAmount = itemView.findViewById(R.id.textViewLoanAmount);
            textViewLoanSub = itemView.findViewById(R.id.textViewLoanSub);
            textViewMonthlyRepayment = itemView.findViewById(R.id.textViewMonthlyRepayment);
            textViewTotalAmountPayable = itemView.findViewById(R.id.textViewTotalAmountPayable);
        }
    }
}
