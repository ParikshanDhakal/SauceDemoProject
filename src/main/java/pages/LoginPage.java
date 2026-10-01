package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * The SauceDemo login page (https://www.saucedemo.com/).
 */
public class LoginPage extends BasePage {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage waitUntilLoaded() {
        waitForVisible(loginButton);
        return this;
    }

    /** Fills in the form and clicks Login. Use for both valid and invalid logins. */
    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }

    /** Logs in and waits for the Products page to load. */
    public InventoryPage loginSuccessfully(String username, String password) {
        login(username, password);
        return new InventoryPage(driver).waitUntilLoaded();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isLoginButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).isDisplayed();
    }
}
