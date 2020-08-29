package com.imat.personalloancalculator;

public class CalculatedLoan {

    public int id;
    public double amount;
    public int term;
    public double rate;
    public double monthly_payment;
    public double total_payable_amount;

    public CalculatedLoan(int id, double amount, int term, double rate, double monthly_payment, double total_payable_amount) {
        this.id = id;
        this.amount = amount;
        this.term = term;
        this.rate = rate;
        this.monthly_payment = monthly_payment;
        this.total_payable_amount = total_payable_amount;
    }
}
