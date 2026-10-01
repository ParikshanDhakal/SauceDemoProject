package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * The cart page (/cart.html).
 */
public class CartPage extends BasePage {

    private final By cartList = By.className("cart_list");
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.cssSelector(".cart_item .inventory_item_name");
    private final By itemPrices = By.cssSelector(".cart_item .inventory_item_price");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("cart.html"));
        waitForVisible(cartList);
        return this;
    }

    public int getItemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> getItemNames() {
        return getAllTexts(itemNames);
    }

    public List<Double> getItemPrices() {
        return getAllPrices(itemPrices);
    }

    public void removeItem(String productName) {
        click(By.xpath(productRowXpath("cart_item", productName) + "//button"));
    }

    public CheckoutInformationPage checkout() {
        click(checkoutButton);
        return new CheckoutInformationPage(driver).waitUntilLoaded();
    }

    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage(driver).waitUntilLoaded();
    }
}
