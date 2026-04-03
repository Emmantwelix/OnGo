package com.group9.ongo.e2e;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewAssertion;

import com.group9.ongo.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class RecyclerViewMatcher {
    private final int recyclerViewId;

    public RecyclerViewMatcher(int recyclerViewId) {
        this.recyclerViewId = recyclerViewId;
    }

    public Matcher<View> atPositionOnView(int position, int targetViewId) {
        return new TypeSafeMatcher<View>() {
            Resources resources = null;
            View childView;

            @Override
            public void describeTo(Description description) {
                String idDescription = Integer.toString(recyclerViewId);
                if (resources != null) {
                    try {
                        idDescription = resources.getResourceName(recyclerViewId);
                    } catch (Resources.NotFoundException ignored) {
                    }
                }

                description.appendText("with id: " + idDescription + " at position: " + position);
            }

            @Override
            protected boolean matchesSafely(View view) {
                resources = view.getResources();

                if (childView == null) {
                    RecyclerView recyclerView = view.getRootView().findViewById(recyclerViewId);
                    if (recyclerView == null || recyclerView.getId() != recyclerViewId) {
                        return false;
                    }

                    RecyclerView.ViewHolder viewHolder =
                            recyclerView.findViewHolderForAdapterPosition(position);

                    if (viewHolder == null) {
                        return false;
                    }

                    childView = viewHolder.itemView;
                }

                if (targetViewId == -1) {
                    return view == childView;
                }

                View targetView = childView.findViewById(targetViewId);
                return view == targetView;
            }
        };
    }

    public static RecyclerViewMatcher withRecyclerView(int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static ViewAssertion allVisibleItemsMatch(String expectedOrigin, String expectedDestination) {
        return (view, noViewFoundException) -> {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;

            int childCount = recyclerView.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View item = recyclerView.getChildAt(i);

                TextView origin = item.findViewById(R.id.originText);
                TextView destination = item.findViewById(R.id.destinationText);

                if (!origin.getText().toString().equals(expectedOrigin)) {
                    throw new AssertionError("Origin mismatch at position " + i +
                            ": expected " + expectedOrigin +
                            " but was " + origin.getText());
                }

                if (!destination.getText().toString().equals(expectedDestination)) {
                    throw new AssertionError("Destination mismatch at position " + i +
                            ": expected " + expectedDestination +
                            " but was " + destination.getText());
                }
            }
        };
    }
}