package com.cpen321.modernwaiter;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;

import com.android.volley.toolbox.StringRequest;
import com.cpen321.modernwaiter.customer.application.ApiUtil;
import com.cpen321.modernwaiter.customer.application.CustomerActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.android.volley.Request.Method.GET;

public class PlaceOrderTest {
    @Before
    public void changeUserAndTableId() throws InterruptedException {
        ApiUtil.notificationEnabled = false;

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CustomerActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt("userId", 2);
        bundle.putString("restaurantId", ApiUtil.RESTAURANT_ID);
        bundle.putString("tableId", "2");

        intent.putExtras(bundle);

        ActivityScenario.launch(intent);
        Thread.sleep(1000);
    }

    /**
     * Check if the server is running
     */
    @Test public void checkServerConnection(){
        new StringRequest(GET, ApiUtil.health, response -> {
            if(response == null) Assert.fail("Received null response from backend health checkup");
            else Assert.assertTrue( true);
        }, error -> {
            Assert.fail("Could not connect to the server" + error.toString());
        });
    }

    /**
     * add item to cart
     * view cart
     * check if the item was added to the cart
     * cleanup : remove item from cart
     */
    @Test
    public void addMenuItemToOrder(){
        //view menu as default (start_destination)
        onView(withId(R.id.fragment_menu))
                .check(matches(isDisplayed()));

        //check that the menu items list is displayed
        onView(withId(R.id.menu_recycler))
                .check(matches(isDisplayed()));

        //make sure that recycler view is displayed
        onView(withId(R.id.menu_recycler))
                .check(matches(isDisplayed()));

        //click on a menu item
        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //check if the menu item detail view is displayed
        onView(withId(R.id.fragment_menu))
                .check(matches(isDisplayed()));
        onView(withId(R.id.topCardView))
                .check(matches(isDisplayed()));

        String addedItem = "Spicy Ahi Roll";
        onView(withId(R.id.incrementButton))
                    .perform(click());

        //now go back to menu
        onView(withId(R.id.exitButton))
                .perform(click());

        //check that in menu fragment
        onView(withId(R.id.fragment_menu))
                .check(matches(isDisplayed()));

        //click view cart
        onView(withId(R.id.viewCartButton))
                .perform(click());

        //check that cart is displayed
        onView(withId(R.id.fragment_order))
                .check(matches(isDisplayed()));

        //check that order_recycler is displayed
        onView(withId(R.id.order_recycler))
                .check(matches(isDisplayed()));

        //check that added item is displayed
        // Attempt to scroll to an item that contains the special text.
        onView(withId(R.id.order_recycler))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(addedItem))
                ));

        //////////cleanup///////////////////

        //now click on menu in bottom navigation bar
        onView(withId(R.id.navigation_menu))
                .perform(click());

        //click on a menu item
        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //remove the item
        onView(withId(R.id.decrementButton))
                .perform(click());

        Assert.assertTrue( true);
    }

    /**
     * add item to cart
     * checkout
     * view bill
     * cleanup : done in next test payForAll
     */
    @Test
    public void addMenuItemAndViewBill() {
        //by default on menu

        //click on a menu item
        onView(withId(R.id.menu_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        String addedItem = "Spicy Ahi Roll";

        //add item to cart
        onView(withId(R.id.incrementButton))
                .perform(click());

        //now go back to menu
        onView(withId(R.id.exitButton))
                .perform(click());
        try{
            onView(withId(R.id.viewCartButton))
                    .check(matches(isDisplayed()));
        } catch(Exception e){
            Assert.fail("View Cart Button is not displayed");
            return;
        }
        try {
            //click view cart
            onView(withId(R.id.viewCartButton))
                    .perform(click());
        }
        catch(Exception e){
            Assert.fail("Click ViewCartButton is not working");
            return;
        }
        //click on checkout
        onView(withId(R.id.checkoutButton))
                .perform(click());

        //click on view bill
        onView(withId(R.id.startBillButton))
                .perform(click());

        //check that its displaying the bill
        onView(withId(R.id.fragment_bill))
                .check(matches(isDisplayed()));

        //check that item is added to the bill
        onView(withId(R.id.bill_recycler))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant((withText(addedItem)))
                ));

        Assert.assertTrue( true);
    }
}
