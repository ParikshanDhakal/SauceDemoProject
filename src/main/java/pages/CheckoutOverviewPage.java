package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Checkout step two: "Checkout: Overview" (/checkout-step-two.html).
 */
public class CheckoutOverviewPage extends BasePage {

    private final By itemNames = By.cssSelector(".cart_item .inventory_item_name");
    private final By itemPrices = By.cssSelector(".cart_item .inventory_item_price");
    private final By itemTotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutOverviewPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
        waitForVisible(totalLabel);
        return this;
    }

    public List<String> getItemNames() {
        return getAllTexts(itemNames);
    }

    public List<Double> getItemPrices() {
        return getAllPrices(itemPrices);
    }

    /** "Item total: $53.97" -> 53.97 */
    public double getItemTotal() {
        return parsePrice(getText(itemTotalLabel));
    }

    /** "Tax: $4.32" -> 4.32 */
    public double getTax() {
        return parsePrice(getText(taxLabel));
    }

    /** "Total: $58.29" -> 58.29 */
    public double getTotal() {
        return parsePrice(getText(totalLabel));
    }

    public CheckoutCompletePage finish() {
        click(finishButton);
        return new CheckoutCompletePage(driver).waitUntilLoaded();
    }
}
