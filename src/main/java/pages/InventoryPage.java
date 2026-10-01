package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * The Products page shown after login (/inventory.html).
 */
public class InventoryPage extends BasePage {

    private final By inventoryList = By.className("inventory_list");
    private final By productCards = By.className("inventory_item");
    private final By productNames = By.className("inventory_item_name");
    private final By productPrices = By.className("inventory_item_price");
    private final By productImages = By.cssSelector("img.inventory_item_img");
    private final By sortDropdown = By.className("product_sort_container");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public InventoryPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        waitForVisible(inventoryList);
        return this;
    }

    public int getProductCount() {
        return driver.findElements(productCards).size();
    }

    public List<String> getProductNames() {
        return getAllTexts(productNames);
    }

    public List<Double> getProductPrices() {
        return getAllPrices(productPrices);
    }

    public List<String> getProductImageSources() {
        List<String> sources = new ArrayList<>();
        for (WebElement image : driver.findElements(productImages)) {
            sources.add(image.getDomAttribute("src"));
        }
        return sources;
    }

    /** Sorts using the dropdown text, e.g. "Price (low to high)" */
    public void sortBy(String optionText) {
        new Select(waitForVisible(sortDropdown)).selectByVisibleText(optionText);
    }

    public double getPriceOf(String productName) {
        By price = By.xpath(productRowXpath("inventory_item", productName)
                + "//div[contains(@class,'inventory_item_price')]");
        return parsePrice(getText(price));
    }

    /** Text of the product's button: "Add to cart" or "Remove" */
    public String getButtonText(String productName) {
        return getText(productButton(productName));
    }

    public void addToCart(String productName) {
        click(productButton(productName));
    }

    public void removeFromCart(String productName) {
        click(productButton(productName));
    }

    public void addProductsToCart(String... productNames) {
        for (String productName : productNames) {
            addToCart(productName);
        }
    }

    public ProductDetailsPage openProduct(String productName) {
        By nameLink = By.xpath(productRowXpath("inventory_item", productName)
                + "//div[contains(@class,'inventory_item_name')]");
        click(nameLink);
        return new ProductDetailsPage(driver).waitUntilLoaded();
    }

    private By productButton(String productName) {
        return By.xpath(productRowXpath("inventory_item", productName) + "//button");
    }
}
