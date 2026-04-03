package com.group9.ongo.e2e;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.group9.ongo.e2e.RecyclerViewMatcher.withRecyclerView;

import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.group9.ongo.R;
import com.group9.ongo.presentation.MainActivity;

import org.junit.Rule;
import org.junit.Test;


public class FlightDetailsE2ETest {


    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void goToSearchScreen() {
        onView(withId(R.id.navigation_search)).perform(click());
        onView(withId(R.id.etDepartingFrom)).check(matches(isDisplayed()));
    }

    @Test
    public void clickingSearchResult_opensFlightDetails_withCorrectData() {
        goToSearchScreen();

        // Perform search
        onView(withId(R.id.etDepartingFrom))
                .perform(replaceText("Winnipeg"), closeSoftKeyboard());

        onView(withId(R.id.etGoingTo))
                .perform(replaceText("Toronto"), closeSoftKeyboard());

        onView(withId(R.id.btnSearch))
                .perform(click());

        // Click first result
        onView(withRecyclerView(R.id.flightRecyclerView)
                .atPositionOnView(0,R.id.originText))
                .perform(click());

        //check the origin and destination
        onView(withId(R.id.text_origin_city))
                .check(matches(withText("Winnipeg")));

        onView(withId(R.id.text_dest_city))
                .check(matches(withText("Toronto")));

        // Ensure key details are populated (not empty)
        onView(withId(R.id.text_airline))
                .check(matches(not(withText(""))));

        onView(withId(R.id.text_flight_id))
                .check(matches(not(withText(""))));

        onView(withId(R.id.text_duration))
                .check(matches(not(withText(""))));

        onView(withId(R.id.text_price))
                .check(matches(not(withText(""))));

        onView(withId(R.id.text_available_seats))
                .check(matches(not(withText(""))));

        onView(withId(R.id.btn_next))
                .check(matches(withText("Book Now")));
    }
}
