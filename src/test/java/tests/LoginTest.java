
package tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.LoginPage;

public class LoginTest extends BaseClass {

    @Test
    public void validLoginTest() {

        LoginPage lp = new LoginPage(driver);

        lp.login("standard_user", "secret_sauce");

        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(currentUrl.contains("inventory"));
    }
    
    @Test
    public void lockedOutUser() {
    	LoginPage lp1 = new LoginPage(driver);
    	lp1.login("locked_out_user", "secret_sauce");
    	
    	String errorMessage = lp1.getErrorMessage();
    	Assert.assertTrue(errorMessage.contains("Sorry, this user has been locked out"));
    	
    }
    
    @Test
    public void problemUser() {
    	LoginPage lp2 = new LoginPage(driver);
    	lp2.login("problem_user", "secret_sauce");
    	
    	
    	String currentUrl = driver.getCurrentUrl();
    	Assert.assertTrue(currentUrl.contains("inventory"));
    	
        boolean isInventoryDisplayed = driver.findElements(
                By.className("inventory_item")
            ).size() > 0;
            Assert.assertTrue(isInventoryDisplayed, "Inventory items are not displayed");
    	
    	
    	
    }
}
