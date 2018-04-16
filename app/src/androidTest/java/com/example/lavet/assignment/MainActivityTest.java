package com.example.lavet.assignment;

import android.support.test.*;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void tester(){
        //Test for initial greeting
        onView(withId(R.id.textView))
                .check(matches(withText(R.string.greeting)));

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        //Test clicking on "About John" menu item, ensure proper changes made to textView
        onView(withText(R.string.menu_item2)).perform(click());

        onView(withId(R.id.textView))
                .check(matches(withText(R.string.about_john)));

        //Clicks back to home and rechecks textView for the greeting
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        onView(withText(R.string.menu_item1)).perform(click());

        onView(withId(R.id.textView))
                .check(matches(withText(R.string.greeting)));

        //Clicks back to home and rechecks textView for the greeting
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        onView(withText(R.string.menu_item3)).perform(click());

        onView(withId(R.id.textView))
                .check(matches(withText(R.string.title)));
    }

}