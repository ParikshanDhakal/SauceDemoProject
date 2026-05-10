
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {

    WebDriver driver;

    By backpackButton =
            By.id("add-to-cart-sauce-labs-backpack");

    By cartBadge =
            By.className("shopping_cart_badge");
    
    By inventoryItems = 
    		By.cssSelector(".inventory_item_name");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addBackpackToCart() {

        driver.findElement(backpackButton).click();
    }

    public String getCartCount() {

        return driver.findElement(cartBadge).getText();
    }
    
    public boolean isInventoryDisplayed() {
    	return driver.findElements(inventoryItems).size() > 0;
    }
    
    public boolean problemUserFirstItem() {
    	try {
    	driver.findElements(inventoryItems).get(0).click();
    	return true;
    	} catch (Exception e) {
    		return false;
    	}
    }
}
