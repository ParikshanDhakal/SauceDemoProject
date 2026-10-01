package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Order confirmation: "Checkout: Complete!" (/checkout-complete.html).
 */
public class CheckoutCompletePage extends BasePage {

    private final By confirmationHeader = By.className("complete-header");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public CheckoutCompletePage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("checkout-complete.html"));
        waitForVisible(confirmationHeader);
        return this;
    }

    public String getConfirmationMessage() {
        return getText(confirmationHeader);
    }

    public InventoryPage backHome() {
        click(backHomeButton);
        return new InventoryPage(driver).waitUntilLoaded();
    }
}
