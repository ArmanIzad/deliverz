package com.arman.deliverz

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arman.deliverz.presentation.deliverydetails.DeliveryDetailsActivity
import com.arman.deliverz.presentation.deliverylist.DeliveryListActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.StringDescription
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class DeliveryListActivityTest {

    @get:Rule
    val rule = activityScenarioRule<DeliveryListActivity>()

    @Before
    fun setup() {
        val scenario = rule.scenario
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @After
    fun tearDown() {
        rule.scenario.close()
    }

    @Test
    @Throws(Exception::class)
    fun testRecyclerView_view_scroll_click() {
        // wait for view to be visible
        onView(withId(R.id.rv_delivery)).perform(waitForView((withId(R.id.rv_delivery))))
        // verify the visibility of recycler view on screen
        onView(withId(R.id.rv_delivery)).check(matches(isDisplayed()))
        val lastPosition = 20
        // scroll to item position 20
        onView(withId(R.id.rv_delivery))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>((lastPosition)));

        // wait for image to load
        Thread.sleep(2000)

        Intents.init()
        // perform click on view at 1 position in RecyclerView
        onView(withId(R.id.rv_delivery)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        // verify CatDetailsActivity was opened
        intended(hasComponent(DeliveryDetailsActivity::class.java.name))
    }

    private fun waitForView(
        viewMatcher: Matcher<View>,
        timeout: Long = 10000,
        waitForDisplayed: Boolean = true
    ): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return Matchers.any(View::class.java)
            }

            override fun getDescription(): String {
                val matcherDescription = StringDescription()
                viewMatcher.describeTo(matcherDescription)
                return "wait for a specific view <$matcherDescription> to be ${if (waitForDisplayed) "displayed" else "not displayed during $timeout millis."}"
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + timeout
                val visibleMatcher = isDisplayed()

                do {
                    val viewVisible = TreeIterables.breadthFirstViewTraversal(view)
                        .any { viewMatcher.matches(it) && visibleMatcher.matches(it) }

                    if (viewVisible == waitForDisplayed) return
                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                // Timeout happens.
                throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException())
                    .build()
            }
        }
    }
}
