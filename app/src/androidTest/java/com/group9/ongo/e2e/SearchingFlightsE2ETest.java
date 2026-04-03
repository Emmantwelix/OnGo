package com.group9.ongo.e2e;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.group9.ongo.business.constants.FlightConstants.AVAILABLE_SEAT;
import static com.group9.ongo.business.constants.FlightConstants.DURATION;
import static com.group9.ongo.business.constants.FlightConstants.PRICE;
import static com.group9.ongo.e2e.RecyclerViewMatcher.allVisibleItemsMatch;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;


import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.group9.ongo.R;
import com.group9.ongo.presentation.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchingFlightsE2ETest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    private void goToSearchScreen() {
        onView(withId(R.id.navigation_search)).perform(click());
        onView(withId(R.id.etDepartingFrom)).check(matches(isDisplayed()));
    }

    @Test
    public void toggleAdvanced_showsAdvancedContainer() {
        goToSearchScreen();

        onView(withId(R.id.toggleAdvanced)).perform(click());

        onView(withId(R.id.advancedContainer))
                .check(matches(withEffectiveVisibility(VISIBLE)));

        onView(withId(R.id.sortSpinner))
                .check(matches(withSpinnerText(containsString(PRICE)))); //default
    }

    @Test
    public void toggleAdvanced_twice_hidesAdvancedContainer() {
        goToSearchScreen();

        onView(withId(R.id.toggleAdvanced)).perform(click());
        onView(withId(R.id.advancedContainer))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.toggleAdvanced)).perform(click());
        onView(withId(R.id.advancedContainer))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void validSearch_showsFlightsList() {
        goToSearchScreen();

        onView(withId(R.id.etDepartingFrom))
                .perform(replaceText("Winnipeg"), closeSoftKeyboard());

        onView(withId(R.id.etGoingTo))
                .perform(replaceText("Toronto"), closeSoftKeyboard());

        onView(withId(R.id.btnSearch))
                .perform(click());

        onView(withId(R.id.flightRecyclerView))
                .check(matches(withEffectiveVisibility(VISIBLE)));

        onView(withId(R.id.noFlightsContainer))
                .check(matches(withEffectiveVisibility(GONE)));
    }

    @Test
    public void validSearch_withAdvancedAndPriceSort_showsFlightList()
    {
        validSearch_WithAdvancedAndField_showsFlights(PRICE);
    }

    @Test
    public void validSearch_withAdvancedAndDurationSort_showsFlightsList() {
        validSearch_WithAdvancedAndField_showsFlights(DURATION);
    }

    @Test
    public void validSearch_withAdvancedAndAvailableSeatsSort_showsFlightList()
    {
        validSearch_WithAdvancedAndField_showsFlights(AVAILABLE_SEAT);
    }

    @Test
    public void invalidSearch_showsNoFlightsMessage() {
        goToSearchScreen();

        onView(withId(R.id.etDepartingFrom))
                .perform(replaceText("Nowhere"), closeSoftKeyboard());

        onView(withId(R.id.etGoingTo))
                .perform(replaceText("Atlantis"), closeSoftKeyboard());

        onView(withId(R.id.btnSearch))
                .perform(click());

        onView(withId(R.id.flightRecyclerView))
                .check(matches(withEffectiveVisibility(GONE)));

        onView(withId(R.id.noFlightsContainer))
                .check(matches(withEffectiveVisibility(VISIBLE)));

        onView(withId(R.id.textNoFlightsError))
                .check(matches(withText("No flights available from Nowhere to Atlantis")));
    }

    @Test
    public void validSearch_displaysExpectedOriginAndDestinationInFirstRow() {
        goToSearchScreen();

        onView(withId(R.id.etDepartingFrom))
                .perform(replaceText("Winnipeg"), closeSoftKeyboard());

        onView(withId(R.id.etGoingTo))
                .perform(replaceText("Toronto"), closeSoftKeyboard());

        onView(withId(R.id.btnSearch))
                .perform(click());

        //ensure results are loaded
        onView(withId(R.id.flightRecyclerView))
                .check(matches(isDisplayed()));

        //check results origin and destination
        onView(withId(R.id.flightRecyclerView))
                .check(allVisibleItemsMatch("Winnipeg", "Toronto"));

    }

    private void validSearch_WithAdvancedAndField_showsFlights(String field)
    {
        goToSearchScreen();

        // Open advanced section
        onView(withId(R.id.toggleAdvanced)).perform(click());

        onView(withId(R.id.advancedContainer))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Select "Field" from spinner
        onView(withId(R.id.sortSpinner))
                .check(matches(isDisplayed()))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is(field)))
                .perform(click());

        // verify selection updated
        onView(withId(R.id.sortSpinner))
                .check(matches(withSpinnerText(containsString(field))));

        // Enter search inputs
        onView(withId(R.id.etDepartingFrom))
                .perform(replaceText("Winnipeg"), closeSoftKeyboard());

        onView(withId(R.id.etGoingTo))
                .perform(replaceText("Toronto"), closeSoftKeyboard());

        // Perform search
        onView(withId(R.id.btnSearch))
                .perform(click());

        // Verify results are shown
        onView(withId(R.id.flightRecyclerView))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.noFlightsContainer))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

}