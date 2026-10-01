package tests;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.CheckoutInformationPage;
import pages.InventoryPage;
import pages.LoginPage;
import pages.ProductDetailsPage;
import utils.TestData;

/**
 * SauceDemo's "problem_user" account has bugs built in on purpose.
 * Each test below checks the CORRECT behavior, so each one is expected to FAIL.
 * A failure (with its screenshot in the report) is the evidence for the matching entry in BUGS.md.
 *
 * Not part of the regression suite. Run with: mvn test -Dsuite=testng-known-bugs.xml
 */
public class ProblemUserBugsTest extends BaseClass {

    private InventoryPage loginAsProblemUser() {
        return new LoginPage(driver).loginSuccessfully(TestData.PROBLEM_USER, TestData.PASSWORD);
    }

    @Test(description = "BUG-01: Every product should show its own image")
    public void productImagesShouldBeDifferent() {
        List<String> imageSources = loginAsProblemUser().getProductImageSources();
        Set<String> uniqueImages = new HashSet<>(imageSources);

        Assert.assertEquals(uniqueImages.size(), TestData.TOTAL_PRODUCTS,
                "Expected 6 different product images but found " + uniqueImages.size() + ": " + uniqueImages);
    }

    @Test(description = "BUG-02: Sorting by Name (Z to A) should reorder the products")
    public void sortingShouldReorderProducts() {
        InventoryPage inventory = loginAsProblemUser();
        inventory.sortBy("Name (Z to A)");

        Assert.assertEquals(inventory.getProductNames().get(0), TestData.RED_T_SHIRT,
                "First product after sorting Z to A");
    }

    @Test(description = "BUG-03: Every product's Add to cart button should add it to the cart")
    public void everyProductShouldBeAddableToCart() {
        InventoryPage inventory = loginAsProblemUser();
        inventory.addProductsToCart(TestData.BACKPACK, TestData.BIKE_LIGHT, TestData.BOLT_T_SHIRT,
                TestData.FLEECE_JACKET, TestData.ONESIE, TestData.RED_T_SHIRT);

        Assert.assertEquals(inventory.getCartCount(), TestData.TOTAL_PRODUCTS,
                "Cart count after clicking Add to cart on all six products");
    }

    @Test(description = "BUG-04: Clicking a product name should open that same product")
    public void productLinkShouldOpenMatchingProduct() {
        ProductDetailsPage details = loginAsProblemUser().openProduct(TestData.BACKPACK);

        Assert.assertEquals(details.getProductName(), TestData.BACKPACK);
    }

    @Test(description = "BUG-05: The Last Name field on checkout should keep what the user types")
    public void lastNameFieldShouldKeepTypedText() {
        InventoryPage inventory = loginAsProblemUser();
        inventory.addToCart(TestData.BIKE_LIGHT);
        CheckoutInformationPage information = inventory.openCart().checkout();

        information.enterInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE);

        Assert.assertEquals(information.getLastNameValue(), TestData.LAST_NAME, "Last Name field value");
        Assert.assertEquals(information.getFirstNameValue(), TestData.FIRST_NAME, "First Name field value");
    }
}
