
package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.InventoryPage;
import pages.LoginPage;

public class AddToCartTest extends BaseClass {

    @Test
    public void addItemToCartTest() {

        LoginPage lp = new LoginPage(driver);

        lp.login("standard_user", "secret_sauce");

        InventoryPage ip = new InventoryPage(driver);

        ip.addBackpackToCart();

        Assert.assertEquals(ip.getCartCount(), "1");
    }
}
