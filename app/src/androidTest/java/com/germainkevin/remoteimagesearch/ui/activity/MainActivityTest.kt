package com.germainkevin.remoteimagesearch.ui.activity

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.germainkevin.remoteimagesearch.R
import com.germainkevin.remoteimagesearch.ui.fragment.gallery.paging.GalleryPagingAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    companion object {
        const val ITEM_POSITION = 7
    }

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun app_bar_is_displayed() {
        onView(withId(R.id.app_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun toolbar_is_displayed() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun nav_host_fragment_is_displayed() {
        onView(withId(R.id.nav_host_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun click_on_settings_in_the_action_bar_overflow_menu() {

        // Open the options menu OR open the overflow menu, depending on whether
        // the device has a hardware or software overflow menu button.
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())

        // Click the item.
        onView(withText(R.string.action_settings)).perform(click())

    }
}