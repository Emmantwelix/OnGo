
package com.group9.ongo.e2e;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.group9.ongo.e2e.RecyclerViewMatcher.withRecyclerView;
import static com.group9.ongo.e2e.SeatSelectionHelper.selectSampleSeat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;

import android.content.Context;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BookingFlightE2ETest {

    private static final String TEST_EMAIL = "espresso@test.com";
    private static final String TEST_PASSWORD = "password123";

    @Rule
    public ActivityScenarioRule<com.group9.ongo.presentation.MainActivity> rule =
            new ActivityScenarioRule<>(com.group9.ongo.presentation.MainActivity.class);

    @Before
    public void setUp() throws ValidationException {
        Context context = getApplicationContext();

        context.deleteDatabase(AppDbHelper.DB_NAME);

        AppDbHelper dbHelper = new AppDbHelper(context, true);

        UserRepository userRepository = new SqlUserRepository(dbHelper);
        UserService userService = new UserServiceImpl(userRepository);

       int userId =  userService.createUser(
                "Espresso User",
                TEST_EMAIL,
                "2045551234",
                TEST_PASSWORD
        );

        context.getSharedPreferences("OngoPrefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("current_user_id", userId)
                .apply();

        syncBookingServiceUser(userId);
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
    public void booking_whenLoggedOut_opensAuthDialog() {

        //logout the user
        Context context = getApplicationContext();
        context.getSharedPreferences("OngoPrefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("current_user_id", -1)
                .apply();

        navigateToUserInfoForFirstFlight();

        onView(withId(R.id.btn_confirm)).perform(click());

        onView(withId(R.id.text_auth_title))
                .check(matches(withText("Sign In")));

    }

    @Test
    public void bookingFlow_canOpenSeatSelection() {
        navigateToUserInfoForFirstFlight();

        onView(withId(R.id.btn_select_seat_user_info))
                .perform(click());

        // Verify SeatSelectionActivity is open
        onView(withId(R.id.seat_map_view))
                .check(matches(isDisplayed()));

        onView(withId(R.id.text_seat_info))
                .check(matches(withText(containsString("Select Your Seat"))));
    }

    @Test
    public void confirmBooking_withoutFirstName_showsFirstNameError() {
        navigateToUserInfoForFirstFlight();

        selectSampleSeat();

        // try booking
        onView(withId(R.id.btn_confirm))
                .perform(click());

        onView(withId(R.id.edit_first_name))
                .check(matches(hasErrorText(not(isEmptyOrNullString()))));
    }

    @Test
    public void confirmBooking_withoutLastName_showsLastNameError() {
        navigateToUserInfoForFirstFlight();

        onView(withId(R.id.edit_first_name))
                .perform(replaceText("John"), closeSoftKeyboard());

        onView(withId(R.id.edit_birth_date))
                .perform(replaceText("2000-05-10"), closeSoftKeyboard());

        onView(withId(R.id.edit_passport_number))
                .perform(replaceText("A1234567"), closeSoftKeyboard());

        selectSampleSeat();

        onView(withId(R.id.btn_confirm)).perform(click());

        onView(withId(R.id.edit_last_name))
                .check(matches(hasErrorText(not(isEmptyOrNullString()))));
    }

    @Test
    public void confirmBooking_withInvalidBirthDate_showsBirthDateError() {
        navigateToUserInfoForFirstFlight();

        onView(withId(R.id.edit_first_name))
                .perform(replaceText("John"), closeSoftKeyboard());

        onView(withId(R.id.edit_last_name))
                .perform(replaceText("Smith"), closeSoftKeyboard());

        onView(withId(R.id.edit_birth_date))
                .perform(replaceText("not-a-date"), closeSoftKeyboard());

        onView(withId(R.id.edit_passport_number))
                .perform(replaceText("A1234567"), closeSoftKeyboard());

        selectSampleSeat();

        onView(withId(R.id.btn_confirm)).perform(click());

        onView(withId(R.id.edit_birth_date))
                .check(matches(hasErrorText(not(isEmptyOrNullString()))));
    }

    @Test
    public void confirmBooking_withoutPassport_showsPassportError() {
        navigateToUserInfoForFirstFlight();

        onView(withId(R.id.edit_first_name))
                .perform(replaceText("John"), closeSoftKeyboard());

        onView(withId(R.id.edit_last_name))
                .perform(replaceText("Smith"), closeSoftKeyboard());

        onView(withId(R.id.edit_birth_date))
                .perform(replaceText("2000-05-10"), closeSoftKeyboard());


        selectSampleSeat();

        onView(withId(R.id.btn_confirm)).perform(click());

        onView(withId(R.id.edit_passport_number))
                .check(matches(hasErrorText(not(isEmptyOrNullString()))));
    }


    private void goToSearchScreen() {
        onView(withId(R.id.navigation_search)).perform(click());
        onView(withId(R.id.etDepartingFrom)).check(matches(isDisplayed()));
    }

    private void navigateToUserInfoForFirstFlight() {
        goToSearchScreen();

        onView(withId(R.id.etDepartingFrom))
                .perform(replaceText("Winnipeg"), closeSoftKeyboard());

        onView(withId(R.id.etGoingTo))
                .perform(replaceText("Toronto"), closeSoftKeyboard());

        onView(withId(R.id.btnSearch))
                .perform(click());

        onView(withId(R.id.flightRecyclerView))
                .check(matches(isDisplayed()));

        // Click first result
        onView(withRecyclerView(R.id.flightRecyclerView)
                .atPositionOnView(0,R.id.originText))
                .perform(click());

        onView(withId(R.id.btn_next))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.edit_first_name))
                .check(matches(isDisplayed()));
    }

    private void syncBookingServiceUser(int testUserId) {
        rule.getScenario().onActivity(activity -> {
            OnGoApp app = (OnGoApp) activity.getApplication();
            app.updateBookingServiceUser(testUserId);
        });
    }

}
