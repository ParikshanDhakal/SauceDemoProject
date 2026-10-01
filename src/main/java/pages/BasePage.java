package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.ConfigReader;

/**
 * Shared helpers for every page: explicit waits, typing, reading text,
 * and the header (page title, cart icon, side menu) that all logged-in pages share.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private final By pageTitle = By.className("title");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("timeout")));
    }

    // ---------- Reusable actions ----------

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement field = waitForVisible(locator);
        field.clear();
        if (!text.isEmpty()) {
            field.sendKeys(text);
        }
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    protected List<String> getAllTexts(By locator) {
        List<String> texts = new ArrayList<>();
        for (WebElement element : driver.findElements(locator)) {
            texts.add(element.getText().trim());
        }
        return texts;
    }

    protected List<Double> getAllPrices(By locator) {
        List<Double> prices = new ArrayList<>();
        for (String text : getAllTexts(locator)) {
            prices.add(parsePrice(text));
        }
        return prices;
    }

    /** Turns "$29.99" or "Item total: $29.99" into 29.99 */
    public static double parsePrice(String text) {
        return Double.parseDouble(text.substring(text.indexOf('$') + 1).trim());
    }

    /** XPath for a product card (or cart row) that contains the given product name */
    protected static String productRowXpath(String rowClass, String productName) {
        return "//div[@class='" + rowClass + "'][.//div[contains(@class,'inventory_item_name')"
                + " and normalize-space()='" + productName + "']]";
    }

    // ---------- Header shared by all logged-in pages ----------

    public String getPageTitle() {
        return getText(pageTitle);
    }

    /** Number shown on the cart icon; 0 when the badge is not shown */
    public int getCartCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        if (badges.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badges.get(0).getText().trim());
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver).waitUntilLoaded();
    }

    public LoginPage logout() {
        click(menuButton);
        click(logoutLink);
        return new LoginPage(driver).waitUntilLoaded();
    }
}
