package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * The details page for one product (/inventory-item.html?id=...).
 */
public class ProductDetailsPage extends BasePage {

    private final By productName = By.cssSelector(".inventory_details_name");
    private final By productPrice = By.cssSelector(".inventory_details_price");
    private final By backButton = By.id("back-to-products");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public ProductDetailsPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("inventory-item.html"));
        waitForVisible(productName);
        return this;
    }

    public String getProductName() {
        return getText(productName);
    }

    public double getProductPrice() {
        return parsePrice(getText(productPrice));
    }

    public InventoryPage backToProducts() {
        click(backButton);
        return new InventoryPage(driver).waitUntilLoaded();
    }
}
