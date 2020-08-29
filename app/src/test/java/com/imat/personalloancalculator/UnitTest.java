package com.imat.personalloancalculator;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    @Test
    public void loanCalculationMethod(){
        try {

            Method calculateLoanMethod = MainActivity.class.getDeclaredMethod("calculateLoan", double.class, int.class, double.class);
            calculateLoanMethod.setAccessible(true);

            ArrayList<CalculatedLoan> expectedCalculatedLoans = new ArrayList<>();
            expectedCalculatedLoans.add(new CalculatedLoan(0, 10000.00, 1, 5.0, 856.07, 10273.00));
            expectedCalculatedLoans.add(new CalculatedLoan(0, 3050000.00, 3, 6.31, 93215.92, 3355773.00));
            expectedCalculatedLoans.add(new CalculatedLoan(0, 10080000.00, 5, 13.49, 231887.32, 13913239.00));

            for(CalculatedLoan expected : expectedCalculatedLoans){
                CalculatedLoan actual = (CalculatedLoan) calculateLoanMethod.invoke(new MainActivity(), expected.amount, expected.term, expected.rate);
                assertEquals(expected.monthly_payment,actual.monthly_payment, 0.5);
                assertEquals(expected.total_payable_amount,actual.total_payable_amount, 0.5);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}