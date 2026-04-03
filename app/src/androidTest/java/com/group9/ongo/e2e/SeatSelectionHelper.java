package com.group9.ongo.e2e;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.group9.ongo.R;
import com.group9.ongo.presentation.SeatMapView;

import org.hamcrest.Matcher;

public class SeatSelectionHelper {
    public static ViewAction selectSeat(final int row, final String label) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "select seat " + row + label;
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof SeatMapView) {
                    ((SeatMapView) view).selectSeat(row, label);
                }
            }
        };
    }

    public static void selectSampleSeat()
    {
        onView(withId(R.id.btn_select_seat_user_info)).perform(click());
        onView(withId(R.id.seat_map_view)).perform(selectSeat(1, "A"));
        onView(withId(R.id.btn_confirm_seat)).perform(click());
    }

}
