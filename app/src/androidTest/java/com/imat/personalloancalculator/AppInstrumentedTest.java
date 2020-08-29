package com.imat.personalloancalculator;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AppInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void NormalUserFlow() {
        double amount = 10000.00;
        int term = 1;
        double rate = 10;

        String amountString = Integer.toString((int) (amount * 100));
        String termString = Integer.toString(term);
        String rateString = Integer.toString((int) (rate * 100));

        onView(withId(R.id.amountInput)).perform(typeText(amountString));
        onView(withId(R.id.termInput)).perform(typeText(termString));
        onView(withId(R.id.interestRateInput)).perform(typeText(rateString), closeSoftKeyboard());
        onView(withId(R.id.calcBtn)).perform(scrollTo(), click());
        onView(withId(R.id.okBtn)).perform(click());
    }

    @Test
    public void InputValidation() {
        onView(withId(R.id.amountInput)).perform(click());
        onView(withId(R.id.termInput)).perform(click());
        onView(withId(R.id.interestRateInput)).perform(click());
        onView(withId(R.id.calcBtn)).perform(scrollTo(), click());
        onView(withId(R.id.TextInputAmountLayout)).check(matches(withErrorText("Amount cannot be empty!")));
        onView(withId(R.id.TextInputTermLayout)).check(matches(withErrorText("Loan term cannot be empty!")));
        onView(withId(R.id.TextInputRateLayout)).check(matches(withErrorText("Interest rate cannot be empty!")));

        double amount = 1000.00;
        String amountString = Integer.toString((int) (amount * 100));
        onView(withId(R.id.amountInput)).perform(typeText(amountString));

        int term = 0;
        String termString = Integer.toString(term);
        onView(withId(R.id.termInput)).perform(typeText(termString));

        double rate = 0;
        String rateString = Integer.toString((int) (rate * 100));
        onView(withId(R.id.interestRateInput)).perform(typeText(rateString), closeSoftKeyboard());

        onView(withId(R.id.calcBtn)).perform(scrollTo(), click());

        onView(withId(R.id.TextInputAmountLayout)).check(matches(withErrorText("Minimum amount is 10,000.00")));
        onView(withId(R.id.TextInputTermLayout)).check(matches(withErrorText("Loan term cannot be 0")));
        onView(withId(R.id.TextInputRateLayout)).check(matches(withErrorText("Interest rate cannot be 0.00")));
    }

    @Test
    public void HelperText() {
        double amount = 1000.00;
        String amountString = Integer.toString((int) (amount * 100));
        onView(withId(R.id.amountInput)).perform(typeText(amountString));
        onView(withId(R.id.TextInputAmountLayout)).check(matches(withHelperText("Minimum amount is 10,000.00")));

        int term = 0;
        String termString = Integer.toString(term);
        onView(withId(R.id.termInput)).perform(typeText(termString));
        onView(withId(R.id.TextInputTermLayout)).check(matches(withHelperText("Loan term cannot be 0")));

        double rate = 0;
        String rateString = Integer.toString((int) (rate * 100));
        onView(withId(R.id.interestRateInput)).perform(typeText(rateString), closeSoftKeyboard());
        onView(withId(R.id.TextInputRateLayout)).check(matches(withHelperText("Interest rate cannot be 0.00")));

    }

    //Custom Matchers
    public static Matcher<View> withHelperText(final String expectedText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }


                CharSequence helperText = ((TextInputLayout) view).getHelperText();

                return expectedText == helperText;
            }
        };
    }

    public static Matcher<View> withErrorText(final String expectedText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence errorText = ((TextInputLayout) view).getError();

                return expectedText == errorText;
            }
        };
    }
}
