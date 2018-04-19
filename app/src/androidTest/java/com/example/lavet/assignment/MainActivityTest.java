package com.example.lavet.assignment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.test.*;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.EditText;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private Activity mActivity;
    private EditText editTextUser;

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void tester(){
        //Test for initial greeting
        onView(withId(R.id.textView))
                .check(matches(withText(R.string.greeting)));

        //Checks form hints and text
        onView(withId(R.id.name))
                .check(matches(withHint(R.string.name)));
        onView(withId(R.id.email))
                .check(matches(withHint(R.string.email)));
        onView(withId(R.id.username))
                .check(matches(withHint(R.string.username)));
        onView(withId(R.id.dob))
                .check(matches(withHint(R.string.dob)));
        onView(withId(R.id.age))
                .check(matches(withText(R.string.age)));
        onView(withId(R.id.registrationButton))
                .check(matches(not(isEnabled())));

        //Type in username
        onView(withId(R.id.username)).perform(typeText("testUserName"));

        //Test reg button status on different DOBs
        checkAge(17);
        checkAge(18);
        checkAge(19);
        
        /*Test clicking on "About John" menu item, ensure proper changes made to textView
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.menu_item2)).perform(click());

        onView(withId(R.id.textView))
                .check(matches(withText(R.string.about_john)));*/

        /*Clicks back to home and rechecks textView for the greeting
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        onView(withText(R.string.menu_item1)).perform(click());

        onView(withId(R.id.textView))
                .check(matches(withText(R.string.greeting)));*/

        /*Clicks back to home and rechecks textView for the greeting
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        onView(withText(R.string.menu_item3)).perform(click());

        onView(withId(R.id.textView))
                .check(matches(withText(R.string.title)));*/
    }

    public static void setDate(int datePickerLaunchViewId, int year, int monthOfYear, int dayOfMonth) {

        onView(withId(datePickerLaunchViewId)).perform(scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }

    //Helper function that sets the calendar based on age and calls test on
    //whether the registration button is enabled.
    public static void checkAge(int age){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR)-age;
        int monthOfYear = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        setDate(R.id.dob, year, monthOfYear, dayOfMonth);

        if(age < 18) {
            onView(withId(R.id.registrationButton))
                    .check(matches(not(isEnabled())));
        }

        if(age >= 18){
            onView(withId(R.id.registrationButton))
                    .check(matches(isEnabled()));
        }
    }


}