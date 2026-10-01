package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.CartPage;
import pages.CheckoutCompletePage;
import pages.CheckoutInformationPage;
import pages.CheckoutOverviewPage;
import pages.InventoryPage;
import utils.TestData;

public class CheckoutTest extends BaseClass {

    @Test(description = "A full purchase ends on the confirmation page and empties the cart")
    public void completePurchaseShowsConfirmation() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addToCart(TestData.BACKPACK);

        CheckoutCompletePage complete = inventory.openCart()
                .checkout()
                .continueWith(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE)
                .finish();

        Assert.assertEquals(complete.getPageTitle(), "Checkout: Complete!");
        Assert.assertEquals(complete.getConfirmationMessage(), "Thank you for your order!");
        Assert.assertEquals(complete.getCartCount(), 0, "Cart should be empty after the order");
    }

    @DataProvider(name = "missingCustomerInfo")
    public Object[][] missingCustomerInfo() {
        return new Object[][] {
            // case name,             first name,           last name,           postal code,           expected error
            { "missing first name",   "",                   TestData.LAST_NAME,  TestData.POSTAL_CODE,  "Error: First Name is required" },
            { "missing last name",    TestData.FIRST_NAME,  "",                  TestData.POSTAL_CODE,  "Error: Last Name is required" },
            { "missing postal code",  TestData.FIRST_NAME,  TestData.LAST_NAME,  "",                    "Error: Postal Code is required" },
        };
    }

    @Test(dataProvider = "missingCustomerInfo",
          description = "Checkout will not continue when a required field is empty")
    public void missingCustomerInfoShowsError(String caseName, String firstName, String lastName,
                                              String postalCode, String expectedError) {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addToCart(TestData.BACKPACK);
        CheckoutInformationPage information = inventory.openCart().checkout();

        information.enterInformation(firstName, lastName, postalCode);
        information.clickContinue();

        Assert.assertEquals(information.getErrorMessage(), expectedError);
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one.html"),
                "Should stay on the information page (" + caseName + ")");
    }

    @Test(description = "The overview's item total equals the sum of the product prices")
    public void itemTotalEqualsSumOfPrices() {
        InventoryPage inventory = loginAsStandardUser();
        double expectedItemTotal = inventory.getPriceOf(TestData.BACKPACK)
                + inventory.getPriceOf(TestData.BOLT_T_SHIRT)
                + inventory.getPriceOf(TestData.ONESIE);
        inventory.addProductsToCart(TestData.BACKPACK, TestData.BOLT_T_SHIRT, TestData.ONESIE);

        CheckoutOverviewPage overview = goToOverview(inventory);

        double sumOfListedPrices = 0;
        for (double price : overview.getItemPrices()) {
            sumOfListedPrices += price;
        }
        Assert.assertEquals(overview.getItemNames().size(), 3);
        Assert.assertEquals(overview.getItemTotal(), sumOfListedPrices, 0.01);
        Assert.assertEquals(overview.getItemTotal(), expectedItemTotal, 0.01);
    }

    @Test(description = "Tax is 8% of the item total, and Total = item total + tax")
    public void totalEqualsItemTotalPlusTax() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addProductsToCart(TestData.FLEECE_JACKET, TestData.BIKE_LIGHT);

        CheckoutOverviewPage overview = goToOverview(inventory);

        double itemTotal = overview.getItemTotal();
        double expectedTax = Math.round(itemTotal * TestData.TAX_RATE * 100) / 100.0;
        Assert.assertEquals(overview.getTax(), expectedTax, 0.01);
        Assert.assertEquals(overview.getTotal(), itemTotal + overview.getTax(), 0.01);
    }

    @Test(description = "Cancel on the information page goes back to the cart with items kept")
    public void cancelCheckoutReturnsToCart() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addToCart(TestData.ONESIE);

        CartPage cart = inventory.openCart().checkout().cancel();

        Assert.assertEquals(cart.getPageTitle(), "Your Cart");
        Assert.assertEquals(cart.getItemCount(), 1);
    }

    private CheckoutOverviewPage goToOverview(InventoryPage inventory) {
        return inventory.openCart()
                .checkout()
                .continueWith(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE);
    }
}
