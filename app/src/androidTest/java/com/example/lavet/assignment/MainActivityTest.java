package com.example.lavet.assignment;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.lavet.assignment.models.Matches;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Calendar;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;


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
        String testName = "Fluffers";
        String testEmail = "rdy2pounce@gmail.com";
        String testUser = "RodentSlayer08";
        String testOccu = "Mouse Catcher";
        String testDesc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam aliquet " +
                "metus nec vulputate bibendum. Quisque sagittis blandit risus nec placerat. Donec " +
                "nec leo ipsum. Sed commodo tempus ligula, pulvinar consectetur nisl feugiat nec. " +
                "\n\nAliquam mollis tortor id lacinia dignissim.";
        onView(withId(R.id.name)).perform(scrollTo()).perform(typeText(testName));
        onView(withId(R.id.email)).perform(scrollTo()).perform(typeText(testEmail));
        onView(withId(R.id.username)).perform(scrollTo()).perform(typeText(testUser));
        onView(withId(R.id.occupation)).perform(scrollTo()).perform(typeText(testOccu));
        onView(withId(R.id.desc)).perform(scrollTo()).perform(typeText(testDesc));

        //Test reg button status on different DOBs
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        checkAge(17, day+1);
        checkAge(18, day);
        checkAge(19, day-1);

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
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.registrationButton))
                .perform(scrollTo()).perform(click());
        onView(withId(R.id.textViewNameAge))
                .check(matches(withText(testName + ", 19")));
        onView(withId(R.id.textViewOccu))
                .check(matches(withText(testOccu)));
        onView(withId(R.id.textViewDesc))
                .check(matches(withText(testDesc)));

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check Settings Tab
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        //Set a reminder time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(07, 22));
        //Set a distance
        onView(withId(R.id.maxDistance)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withId(R.id.maxDistance)).check(matches(withSpinnerText(containsString("12500"))));
        //Set a gender
        onView(withId(R.id.gender)).perform(click());
        onData(anything()).atPosition(4).perform(click());
        onView(withId(R.id.gender)).check(matches(withSpinnerText(containsString("Nonconforming"))));
        //Set Privacy
        onView(withId(R.id.publicButton)).perform(click());
        onView(withId(R.id.publicButton)).check(matches(isChecked()));
        onView(withId(R.id.privateButton)).check(matches(isNotChecked()));
        onView(withId(R.id.privateButton)).perform(click());
        //Set age range
        //Todo figure out how to test this
        //Submit settings
        onView(withId(R.id.submitButton)).perform(scrollTo()).perform(click());
        onView(withText("Settings saved!"))
                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        //Check Matches Tab, Like Button, Toast
        onView(withId(R.id.viewpager)).perform(swipeRight());

        onView(withId(R.id.my_recycler_view)).perform(
                RecyclerViewActions
                        .actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.like_button)));

        onView(withText(containsString("Cool Guy Mike")))
                .inRoot(withDecorView(not(testRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        onView(withId(R.id.my_recycler_view)).perform(
                RecyclerViewActions
                        .actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.like_button)));


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

        //Go back to settings page to check data retention
        checkAge(19, day);
        onView(withId(R.id.registrationButton))
                .perform(scrollTo()).perform(click());
        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.viewpager)).perform(swipeLeft());

    }

    public static void setDate(int datePickerLaunchViewId, int year, int monthOfYear, int dayOfMonth) {

        onView(withId(datePickerLaunchViewId)).perform(scrollTo()).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }

    //Helper function that sets the calendar based on age and calls test on
    //whether the registration button is enabled.
    public static void checkAge(int age, int dayOfMonth){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR)-age;
        int monthOfYear = cal.get(Calendar.MONTH);
        //int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

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

    public static class MyViewAction {

        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }
    }
}