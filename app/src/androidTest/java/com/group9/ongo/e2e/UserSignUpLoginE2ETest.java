package com.group9.ongo.e2e;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.group9.ongo.e2e.EspressoWait.waitFor;
import static org.hamcrest.Matchers.containsString;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.group9.ongo.R;
import com.group9.ongo.application.OnGoApp;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.persistence.real.AppDbHelper;
import com.group9.ongo.presentation.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserSignUpLoginE2ETest {

    private static final String TEST_USER_NAME = "Login User";
    private static final String TEST_EMAIL = "login@test.com";
    private static final String TEST_PHONE = "2045559999";
    private static final String TEST_PASSWORD = "password123";

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws ValidationException {
        Context context = getApplicationContext();
        context.deleteDatabase(AppDbHelper.DB_NAME);

        // Reset SharedPreferences
        context.getSharedPreferences("OngoPrefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("current_user_id", -1)
                .apply();

        rule.getScenario().onActivity(activity -> {
            OnGoApp app = (OnGoApp) activity.getApplication();
            // Completely re-initialize services to use a fresh database connection
            app.initializeServices();
            
            // Pre-create a user for login tests using the newly initialized services
            try {
                app.getUserService().createUser(TEST_USER_NAME, TEST_EMAIL, TEST_PHONE, TEST_PASSWORD);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
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
    public void login_success_showsWelcomeMessage() {
        onView(isRoot()).perform(waitFor(1000));

        // 1. Click Sign In on Home screen
        onView(withId(R.id.button_signin)).perform(click());

        // 2. Enter credentials
        onView(withId(R.id.edit_email)).perform(replaceText(TEST_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.edit_password)).perform(replaceText(TEST_PASSWORD), closeSoftKeyboard());

        // 3. Submit
        onView(withId(R.id.button_auth_submit)).perform(click());

        // 4. Verify welcome message on Home screen
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.text_welcome))
                .check(matches(withText(containsString("Welcome, " + TEST_USER_NAME))));
    }

    @Test
    public void signUp_thenLogin_success() {
        onView(isRoot()).perform(waitFor(1000));

        // 1. Click Sign In on Home screen
        onView(withId(R.id.button_signin)).perform(click());

        // 2. Switch to Sign Up
        onView(withId(R.id.text_switch_auth)).perform(click());

        // 3. Enter new user details
        String newName = "New User";
        String newEmail = "new@test.com";
        onView(withId(R.id.edit_name)).perform(replaceText(newName), closeSoftKeyboard());
        onView(withId(R.id.edit_phone)).perform(replaceText("2045550000"), closeSoftKeyboard());
        onView(withId(R.id.edit_email)).perform(replaceText(newEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_password)).perform(replaceText("pass123"), closeSoftKeyboard());

        // 4. Submit Sign Up
        onView(withId(R.id.button_auth_submit)).perform(click());

        // 5. App should switch back to Sign In automatically
        onView(withId(R.id.text_auth_title)).check(matches(withText("Sign In")));

        // 6. Login with new credentials
        onView(withId(R.id.edit_email)).perform(replaceText(newEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_password)).perform(replaceText("pass123"), closeSoftKeyboard());
        onView(withId(R.id.button_auth_submit)).perform(click());

        // 7. Verify welcome message
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.text_welcome))
                .check(matches(withText(containsString("Welcome, " + newName))));
    }

    @Test
    public void logout_clearsSession_andShowsSignInButton() {
        // 1. Login first
        login_success_showsWelcomeMessage();

        // 2. Go to Settings
        onView(withId(R.id.navigation_settings)).perform(click());

        // 3. Click Sign Out
        onView(withId(R.id.button_sign_out)).perform(click());

        // 4. Verify back on Home and Sign In button is visible
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.button_signin)).check(matches(isDisplayed()));
        onView(withId(R.id.text_welcome)).check(matches(withText("Welcome to Ongo")));
    }
}
