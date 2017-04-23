package automationFramework.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author chaitu
 * 
 * Class to represent google home page
 */
public class GoogleHomePage {
	public void searchGoogleForKeyword(WebDriver driver, String searchKeyword) {
		/* Searches for given keyword in google */
		WebElement searchElement = driver.findElement(By.name("q"));
		searchElement.clear();
		searchElement.sendKeys(searchKeyword);
		searchElement.sendKeys(Keys.ENTER);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("navcnt")));
	}
}
