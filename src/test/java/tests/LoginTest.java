package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.TestData;

public class LoginTest extends BaseClass {

    @Test(description = "A valid user logs in and lands on the Products page")
    public void validLoginOpensProductsPage() {
        InventoryPage inventory = new LoginPage(driver)
                .loginSuccessfully(TestData.STANDARD_USER, TestData.PASSWORD);

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"),
                "Should be on the inventory page after login");
        Assert.assertEquals(inventory.getPageTitle(), "Products");
    }

    @Test(description = "problem_user can still log in and see products")
    public void problemUserCanLogIn() {
        InventoryPage inventory = new LoginPage(driver)
                .loginSuccessfully(TestData.PROBLEM_USER, TestData.PASSWORD);

        Assert.assertEquals(inventory.getProductCount(), TestData.TOTAL_PRODUCTS);
    }

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        String noMatch = "Epic sadface: Username and password do not match any user in this service";
        return new Object[][] {
            // case name,         username,                  password,             expected error
            { "locked out user",  TestData.LOCKED_OUT_USER,  TestData.PASSWORD,    "Epic sadface: Sorry, this user has been locked out." },
            { "wrong password",   TestData.STANDARD_USER,    "wrong_password",     noMatch },
            { "unknown username", "not_a_real_user",         TestData.PASSWORD,    noMatch },
            { "empty username",   "",                        TestData.PASSWORD,    "Epic sadface: Username is required" },
            { "empty password",   TestData.STANDARD_USER,    "",                   "Epic sadface: Password is required" },
        };
    }

    @Test(dataProvider = "invalidLogins",
          description = "Invalid logins show the correct error and stay on the login page")
    public void invalidLoginShowsError(String caseName, String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        Assert.assertEquals(loginPage.getErrorMessage(), expectedError);
        Assert.assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                "User should not get past the login page (" + caseName + ")");
    }

    @Test(description = "Logging out from the menu returns the user to the login page")
    public void logoutReturnsToLoginPage() {
        LoginPage loginPage = loginAsStandardUser().logout();

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button should be shown after logout");
        Assert.assertEquals(driver.getCurrentUrl(), ConfigReader.get("url"));
    }

    @Test(description = "Opening the Products page URL without logging in is blocked")
    public void productsPageRequiresLogin() {
        driver.get(ConfigReader.get("url") + "inventory.html");
        LoginPage loginPage = new LoginPage(driver).waitUntilLoaded();

        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: You can only access '/inventory.html' when you are logged in.");
    }
}
