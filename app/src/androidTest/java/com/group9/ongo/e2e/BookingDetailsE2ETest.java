package com.group9.ongo.e2e;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.group9.ongo.e2e.EspressoWait.waitFor;
import static com.group9.ongo.e2e.RecyclerViewMatcher.withRecyclerView;
import static com.group9.ongo.e2e.SeatSelectionHelper.selectSeat;
import static org.hamcrest.Matchers.containsString;

import android.content.Context;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.services.Interfaces.UserService;
import com.group9.ongo.business.services.Implementations.UserServiceImpl;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.persistence.UserRepository;
import com.group9.ongo.persistence.real.AppDbHelper;
import com.group9.ongo.persistence.real.SqlUserRepository;
import com.group9.ongo.presentation.MainActivity;
import com.group9.ongo.presentation.SeatMapView;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BookingDetailsE2ETest {

    private static final String TEST_EMAIL = "details@test.com";
    private static final String TEST_PASSWORD = "password123";

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws ValidationException {
        Context context = getApplicationContext();
        context.deleteDatabase(AppDbHelper.DB_NAME);

        AppDbHelper dbHelper = new AppDbHelper(context, true);
        UserRepository userRepository = new SqlUserRepository(dbHelper);
        UserService userService = new UserServiceImpl(userRepository);

        int userId = userService.createUser(
                "Details User",
                TEST_EMAIL,
                "2045550000",
                TEST_PASSWORD
        );

        context.getSharedPreferences("OngoPrefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("current_user_id", userId)
                .apply();

        rule.getScenario().onActivity(activity -> {
            OnGoApp app = (OnGoApp) activity.getApplication();
            app.initializeServices();
            app.updateBookingServiceUser(userId);
        });
    }

    @After
    public void tearDown() {
        Context context = getApplicationContext();
        context.getSharedPreferences("OngoPrefs", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
        context.deleteDatabase(AppDbHelper.DB_NAME);
    }

    @Test
    public void bookingDetails_canModifyAndCancelBooking() {
        // 1. Book a flight first
        bookAFlight("Alice", "Smith", "1990-01-01", "P1234567");
        onView(isRoot()).perform(waitFor(1000));

        // 2. Click on the booking in HomeFragment
        onView(withRecyclerView(R.id.recycler_bookings)
                .atPositionOnView(0, R.id.text_passenger_name))
                .perform(click());

        // 3. Verify Detail screen shows correct info
        onView(withId(R.id.text_passenger_name)).check(matches(withText(containsString("Alice Smith"))));
        onView(withId(R.id.text_passport_number)).check(matches(withText("P1234567")));

        // 4. Modify the booking
        onView(withId(R.id.btn_modify_booking)).perform(click());

        // Change first name in BottomSheet
        onView(withId(R.id.edit_first_name))
                .perform(replaceText("Alicia"), closeSoftKeyboard());

        onView(withId(R.id.btn_save_changes)).perform(click());

        // 5. Verify the detail screen is updated
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.text_passenger_name)).check(matches(withText(containsString("Alicia Smith"))));

        // 6. Cancel the booking
        onView(withId(R.id.btn_cancel_booking)).perform(click());

        // 7. Verify we are back on Home screen and the booking is GONE (moved to cancelled page)
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.text_home_title)).check(matches(isDisplayed()));
        onView(withText("Alicia Smith")).check(doesNotExist());

        // 8. Go to Settings and view cancelled flights
        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.card_view_cancelled)).perform(click());

        // 9. Verify the cancelled booking is present in CancelledBookingsActivity
        onView(withId(R.id.recycler_cancelled_bookings)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.recycler_cancelled_bookings)
                .atPositionOnView(0, R.id.text_passenger_name))
                .check(matches(withText(containsString("Alicia Smith"))));
        
        onView(withRecyclerView(R.id.recycler_cancelled_bookings)
                .atPositionOnView(0, R.id.text_status))
                .check(matches(withText(containsString("CANCELLED"))));
    }

    private void bookAFlight(String firstName, String lastName, String dob, String passport) {
        // Go to search
        onView(withId(R.id.navigation_search)).perform(click());

        // Perform search
        onView(withId(R.id.etDepartingFrom)).perform(replaceText("Winnipeg"), closeSoftKeyboard());
        onView(withId(R.id.etGoingTo)).perform(replaceText("Toronto"), closeSoftKeyboard());
        onView(withId(R.id.btnSearch)).perform(click());

        // Select flight
        onView(withId(R.id.flightRecyclerView)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.flightRecyclerView).atPositionOnView(0, R.id.originText)).perform(click());
        onView(withId(R.id.btn_next)).perform(click());

        // Fill passenger info
        onView(withId(R.id.edit_first_name)).perform(scrollTo(), replaceText(firstName), closeSoftKeyboard());
        onView(withId(R.id.edit_last_name)).perform(scrollTo(), replaceText(lastName), closeSoftKeyboard());
        onView(withId(R.id.edit_birth_date)).perform(scrollTo(), replaceText(dob), closeSoftKeyboard());
        onView(withId(R.id.edit_passport_number)).perform(scrollTo(), replaceText(passport), closeSoftKeyboard());

        // Select seat
        onView(withId(R.id.btn_select_seat_user_info)).perform(scrollTo(), click());
        onView(withId(R.id.seat_map_view)).perform(selectSeat(1, "A"));
        onView(withId(R.id.btn_confirm_seat)).perform(click());

        // Confirm booking
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.btn_confirm)).perform(click());
    }

}
