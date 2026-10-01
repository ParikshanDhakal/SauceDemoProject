package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.InventoryPage;
import pages.ProductDetailsPage;
import utils.TestData;

public class InventoryTest extends BaseClass {

    @Test(description = "The Products page lists all six products, each with a name and a price")
    public void productsPageListsAllProducts() {
        InventoryPage inventory = loginAsStandardUser();

        Assert.assertEquals(inventory.getProductCount(), TestData.TOTAL_PRODUCTS);
        for (String name : inventory.getProductNames()) {
            Assert.assertFalse(name.isEmpty(), "Every product should have a name");
        }
        for (double price : inventory.getProductPrices()) {
            Assert.assertTrue(price > 0, "Every product should have a price above $0");
        }
    }

    @Test(description = "Products are sorted by name A to Z by default")
    public void defaultSortIsNameAToZ() {
        List<String> names = loginAsStandardUser().getProductNames();

        Assert.assertEquals(names, sortedCopy(names, Comparator.naturalOrder()));
    }

    @Test(description = "Sorting by Name (Z to A) reverses the alphabetical order")
    public void sortByNameZToA() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.sortBy("Name (Z to A)");

        List<String> names = inventory.getProductNames();
        Assert.assertEquals(names, sortedCopy(names, Comparator.reverseOrder()));
        Assert.assertEquals(names.get(0), TestData.RED_T_SHIRT);
    }

    @Test(description = "Sorting by Price (low to high) puts the cheapest product first")
    public void sortByPriceLowToHigh() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.sortBy("Price (low to high)");

        List<Double> prices = inventory.getProductPrices();
        Assert.assertEquals(prices, sortedCopy(prices, Comparator.naturalOrder()));
    }

    @Test(description = "Sorting by Price (high to low) puts the most expensive product first")
    public void sortByPriceHighToLow() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.sortBy("Price (high to low)");

        List<Double> prices = inventory.getProductPrices();
        Assert.assertEquals(prices, sortedCopy(prices, Comparator.reverseOrder()));
    }

    @Test(description = "Clicking a product name opens that product's details page with the same price")
    public void productNameOpensMatchingDetailsPage() {
        InventoryPage inventory = loginAsStandardUser();
        double listPrice = inventory.getPriceOf(TestData.BACKPACK);

        ProductDetailsPage details = inventory.openProduct(TestData.BACKPACK);

        Assert.assertEquals(details.getProductName(), TestData.BACKPACK);
        Assert.assertEquals(details.getProductPrice(), listPrice, 0.001);
    }

    private static <T> List<T> sortedCopy(List<T> list, Comparator<? super T> order) {
        List<T> copy = new ArrayList<>(list);
        Collections.sort(copy, order);
        return copy;
    }
}
