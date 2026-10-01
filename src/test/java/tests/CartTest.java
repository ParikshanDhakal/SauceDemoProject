package tests;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.CartPage;
import pages.InventoryPage;
import utils.TestData;

public class CartTest extends BaseClass {

    @Test(description = "Adding one product shows 1 on the cart icon and changes the button to Remove")
    public void addOneProductUpdatesCartBadge() {
        InventoryPage inventory = loginAsStandardUser();
        Assert.assertEquals(inventory.getCartCount(), 0, "Cart should start empty");

        inventory.addToCart(TestData.BACKPACK);

        Assert.assertEquals(inventory.getCartCount(), 1);
        Assert.assertEquals(inventory.getButtonText(TestData.BACKPACK), "Remove");
    }

    @Test(description = "Adding three products shows 3 on the cart icon")
    public void addThreeProductsUpdatesCartBadge() {
        InventoryPage inventory = loginAsStandardUser();

        inventory.addProductsToCart(TestData.BACKPACK, TestData.BIKE_LIGHT, TestData.ONESIE);

        Assert.assertEquals(inventory.getCartCount(), 3);
    }

    @Test(description = "Removing products from the Products page lowers the count, then hides the badge")
    public void removeFromProductsPageUpdatesCartBadge() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addProductsToCart(TestData.BACKPACK, TestData.BIKE_LIGHT);

        inventory.removeFromCart(TestData.BACKPACK);
        Assert.assertEquals(inventory.getCartCount(), 1);
        Assert.assertEquals(inventory.getButtonText(TestData.BACKPACK), "Add to cart");

        inventory.removeFromCart(TestData.BIKE_LIGHT);
        Assert.assertEquals(inventory.getCartCount(), 0, "Badge should disappear when the cart is empty");
    }

    @Test(description = "The cart lists exactly the products that were added, at the same prices")
    public void cartShowsAddedProductsAndPrices() {
        InventoryPage inventory = loginAsStandardUser();
        double backpackPrice = inventory.getPriceOf(TestData.BACKPACK);
        double bikeLightPrice = inventory.getPriceOf(TestData.BIKE_LIGHT);
        inventory.addProductsToCart(TestData.BACKPACK, TestData.BIKE_LIGHT);

        CartPage cart = inventory.openCart();

        Assert.assertEquals(cart.getPageTitle(), "Your Cart");
        Assert.assertEquals(cart.getItemNames(), Arrays.asList(TestData.BACKPACK, TestData.BIKE_LIGHT));
        List<Double> prices = cart.getItemPrices();
        Assert.assertEquals(prices.get(0), backpackPrice, 0.001);
        Assert.assertEquals(prices.get(1), bikeLightPrice, 0.001);
    }

    @Test(description = "Removing a product inside the cart takes it out of the cart and the badge")
    public void removeProductInsideCart() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addProductsToCart(TestData.BACKPACK, TestData.FLEECE_JACKET);
        CartPage cart = inventory.openCart();

        cart.removeItem(TestData.BACKPACK);

        Assert.assertEquals(cart.getItemCount(), 1);
        Assert.assertEquals(cart.getItemNames(), Arrays.asList(TestData.FLEECE_JACKET));
        Assert.assertEquals(cart.getCartCount(), 1);
    }

    @Test(description = "Items stay in the cart after going back with Continue Shopping")
    public void cartKeepsItemsAfterContinueShopping() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addToCart(TestData.BOLT_T_SHIRT);

        inventory = inventory.openCart().continueShopping();
        Assert.assertEquals(inventory.getCartCount(), 1);
        Assert.assertEquals(inventory.getButtonText(TestData.BOLT_T_SHIRT), "Remove");

        CartPage cart = inventory.openCart();
        Assert.assertEquals(cart.getItemNames(), Arrays.asList(TestData.BOLT_T_SHIRT));
    }
}
