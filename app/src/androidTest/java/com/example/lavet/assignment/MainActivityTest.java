package com.example.lavet.assignment;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.concurrent.CompletableFuture.allOf;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

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
        onView(withId(R.id.desc))
                .check(matches(withHint(R.string.desc)));
        onView(withId(R.id.age))
                .check(matches(withText(R.string.age)));
        onView(withId(R.id.registrationButton))
                .check(matches(not(isEnabled())));

        //Add text to fields
        String testName = "testName";
        String testEmail = "test@email.com";
        String testUser = "testUserName";
        String testOccu = "Superhero";
        String testDesc = "John is currently a field service engineer for a proton therapy " +
                "center in Seattle, WA. He is studying Application Development at North Seattle College.\n\nHere is another line!";
        onView(withId(R.id.name)).perform(scrollTo()).perform(typeText(testName));
        onView(withId(R.id.email)).perform(scrollTo()).perform(typeText(testEmail));
        onView(withId(R.id.username)).perform(scrollTo()).perform(typeText(testUser));
        onView(withId(R.id.occupation)).perform(scrollTo()).perform(typeText(testOccu));
        onView(withId(R.id.desc)).perform(scrollTo()).perform(typeText(testDesc));

        //Test reg button status on different DOBs
        checkAge(17);
        checkAge(18);
        checkAge(19);

        //Test that data retention during rotation
        testRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.name))
                .check(matches(withText(testName)));
        onView(withId(R.id.email))
                .check(matches(withText(testEmail)));
        onView(withId(R.id.username))
                .check(matches(withText(testUser)));
        onView(withId(R.id.desc))
                .check(matches(withText(testDesc)));
        testRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Click the registration button and check second activity
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.registrationButton))
                .perform(scrollTo()).perform(click());
        onView(withId(R.id.textViewNameAge))
                .check(matches(withText(testName + ", 19")));
        onView(withId(R.id.textViewOccu))
                .check(matches(withText(testOccu)));
        onView(withId(R.id.textViewDesc))
                .check(matches(withText(testDesc)));

        //Check tabs and fragments
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.matchText))
                .check(matches(withText(R.string.matchesPlaceHolder)));
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.settingText))
                .check(matches(withText(R.string.settingPlaceHolder)));

        //Test back button functionality
        Espresso.pressBack();
        onView(withId(R.id.textView))
                .check(matches(withText(R.string.greeting)));
        //Check that form is empty
        onView(withId(R.id.name))
                .check(matches(withText("")));
        onView(withId(R.id.email))
                .check(matches(withText("")));
        onView(withId(R.id.username))
                .check(matches(withText("")));
        onView(withId(R.id.occupation))
                .check(matches(withText("")));
        onView(withId(R.id.desc))
                .check(matches(withText("")));
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