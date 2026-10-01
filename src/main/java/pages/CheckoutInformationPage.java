package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Checkout step one: "Checkout: Your Information" (/checkout-step-one.html).
 */
public class CheckoutInformationPage extends BasePage {

    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutInformationPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
        waitForVisible(firstNameField);
        return this;
    }

    public void enterInformation(String firstName, String lastName, String postalCode) {
        type(firstNameField, firstName);
        type(lastNameField, lastName);
        type(postalCodeField, postalCode);
    }

    public void clickContinue() {
        click(continueButton);
    }

    /** Fills in the form, clicks Continue and waits for the Overview page. */
    public CheckoutOverviewPage continueWith(String firstName, String lastName, String postalCode) {
        enterInformation(firstName, lastName, postalCode);
        clickContinue();
        return new CheckoutOverviewPage(driver).waitUntilLoaded();
    }

    public String getFirstNameValue() {
        return waitForVisible(firstNameField).getDomProperty("value");
    }

    public String getLastNameValue() {
        return waitForVisible(lastNameField).getDomProperty("value");
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public CartPage cancel() {
        click(cancelButton);
        return new CartPage(driver).waitUntilLoaded();
    }
}
